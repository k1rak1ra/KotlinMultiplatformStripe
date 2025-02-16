package net.k1ra.kotlinmultiplatformstripe

import androidx.compose.runtime.Composable
import com.stripe.android.paymentsheet.ExperimentalCustomerSessionApi
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

actual class PaymentSheet(
    val sheet: PaymentSheet
)

@OptIn(ExperimentalCustomerSessionApi::class)
actual fun presentStripePaymentSheet(
    sheet: net.k1ra.kotlinmultiplatformstripe.PaymentSheet,
    merchantConfig: MerchantConfiguration,
    paymentIntentClientSecret: String,
    customerConfig: CustomerConfiguration?,
) {
    val stripeCustomerConfig = if (customerConfig != null) {
        if (customerConfig.accessType is CustomerAccessType.LegacyCustomerEphemeralKey)
            PaymentSheet.CustomerConfiguration(customerConfig.id, customerConfig.ephemeralKeySecret)
        else
            PaymentSheet.CustomerConfiguration.createWithCustomerSession(customerConfig.id, (customerConfig.accessType as CustomerAccessType.CustomerSession).customerSessionClientSecret)
    } else {
        null
    }

    sheet.sheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = merchantConfig.merchantDisplayName,
            customer = stripeCustomerConfig,
            allowsDelayedPaymentMethods = merchantConfig.allowsDelayedPaymentMethods
        )
    )
}

@Composable
actual fun rememberPaymentSheet(resultHandler: PaymentSheetResultHandler): net.k1ra.kotlinmultiplatformstripe.PaymentSheet {
    return PaymentSheet(rememberPaymentSheet { paymentSheetResult ->
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> resultHandler.onCanceled()
            is PaymentSheetResult.Failed -> resultHandler.onFailed(paymentSheetResult.error)
            is PaymentSheetResult.Completed -> resultHandler.onCompleted()
        }
    })
}
