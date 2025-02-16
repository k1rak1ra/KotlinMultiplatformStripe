package net.k1ra.kotlinmultiplatformstripe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.ErrorEvent
import org.w3c.dom.HTMLElement
import kotlin.js.Promise

actual class PaymentSheet {
    val showPaymentSheet = mutableStateOf(false)
    val htmlHeight = mutableStateOf(0.0)
    var elements: Elements? = null
}

var paymentSheetInstance = PaymentSheet()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun rememberPaymentSheet(resultHandler: PaymentSheetResultHandler): PaymentSheet {
    val showSheet = remember { paymentSheetInstance.showPaymentSheet }
    val htmlHeight = remember { paymentSheetInstance.htmlHeight }
    val isLoading = remember { mutableStateOf(false) }

    if (showSheet.value) {
        //Stupid hack to dynamically get the height of the HTML element, there's probably a better way of doing this
        CoroutineScope(Dispatchers.Main).launch {
            while(paymentSheetInstance.showPaymentSheet.value) {
                val element = document.getElementById("payment-element")
                val height = element?.getBoundingClientRect()?.height

                if (height != null)
                    htmlHeight.value = height

                delay(10)
            }
        }

        ModalBottomSheet(
            sheetState = SheetState(
                skipPartiallyExpanded = true,
                density = LocalDensity.current
            ),
            onDismissRequest = {
                resultHandler.onCanceled()
                showSheet.value = false
            }
        ) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                HtmlView(
                    modifier = Modifier.fillMaxWidth().height((htmlHeight.value).dp).padding(16.dp),
                    factory = {
                        val container = document.createElement("div") as HTMLElement
                        container.innerHTML = "<div id=\"payment-element\"></div>"
                        container
                    }
                )

                Box(Modifier.height(16.dp))

                if (isLoading.value) {
                    LinearProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            isLoading.value = true

                            var redirectUrl = window.location.href.replace("index.html","")
                            if (!redirectUrl.endsWith("/"))
                                redirectUrl += "/"
                            redirectUrl += "complete.html"

                            makePaymentConfirmationPromise(PaymentConfiguration.stripeInstance!!, paymentSheetInstance.elements!!, redirectUrl).then {
                                val error = extractErrorFromObj(it)
                                val paymentIntent = extractPaymentIntentFromObj(it)

                                //The payment form seems to show these errors, so we'll ignore them
                                if (error == null && paymentIntent != null) {
                                    showSheet.value = false
                                    resultHandler.onCompleted()
                                }

                                isLoading.value = false
                                null
                            }.catch {
                                resultHandler.onFailed(Exception("Failed to process payment"))

                                isLoading.value = false
                                null
                            }
                        }
                    ) {
                        Text("Submit payment")
                    }
                }
            }
        }
    }

    return paymentSheetInstance
}

actual fun presentStripePaymentSheet(
    sheet: PaymentSheet,
    merchantConfig: MerchantConfiguration,
    paymentIntentClientSecret: String,
    customerConfig: CustomerConfiguration?,
) {
    paymentSheetInstance.showPaymentSheet.value = true

    CoroutineScope(Dispatchers.Main).launch {
        //Wait for animation and JS mount, stupid hack
        delay(100)

        paymentSheetInstance.elements = mountPaymentElement(paymentIntentClientSecret, PaymentConfiguration.stripeInstance!!)
    }
}

fun mountPaymentElement(pIcS: String, stripe: Stripe) : Elements {
    js("""
        const appearanceObj = {
            theme: 'night',
            labels: 'floating'
        };
        
        const options = {
            clientSecret: pIcS,
            appearance: appearanceObj
        };

        // Set up Stripe.js and Elements to use in checkout form, passing the client secret obtained in a previous step
        const elements = stripe.elements(options);

        // Create and mount the Payment Element
        const paymentElementOptions = { layout: 'tabs'};
        const paymentElement = elements.create('payment', paymentElementOptions);
        paymentElement.mount('#payment-element');
        
        return elements;
    """)
}

fun makePaymentConfirmationPromise(stripe: Stripe, elements: Elements, redirectUrl: String) : Promise<JsAny> {
    js("""
        return stripe.confirmPayment({
            //`Elements` instance that was used to create the Payment Element
            elements,
            confirmParams: {
                return_url: redirectUrl,
            },
            redirect: 'if_required'
        });
    """)
}

fun extractErrorFromObj(obj: JsAny) : ErrorEvent? {
    js("""
        return obj.error;
    """)
}

fun extractPaymentIntentFromObj(obj: JsAny) : PaymentIntent? {
    js("""
        return obj.paymentIntent;
    """)
}