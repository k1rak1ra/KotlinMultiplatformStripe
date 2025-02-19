package net.k1ra.kotlinmultiplatformstripe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stripe.android.model.ConfirmSetupIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.StripeIntent
import com.stripe.android.payments.bankaccount.CollectBankAccountConfiguration
import com.stripe.android.payments.bankaccount.CollectBankAccountLauncher
import com.stripe.android.payments.bankaccount.navigation.CollectBankAccountResult
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult

var setupRequest: CollectBankAccountSetupRequest? = null
var globalResultHandler: CollectBankAccountResultHandler? = null

class BankAccountLinkActivity : AppCompatActivity() {
    var methodId: String? = null
    private lateinit var setupIntentClientSecret: String
    private lateinit var collectBankAccountLauncher: CollectBankAccountLauncher
    private val paymentLauncher = PaymentLauncher.Companion.create(this, publishableKeyInst, null, ::onPaymentResult)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        collectBankAccountLauncher = CollectBankAccountLauncher.create(this) { result: CollectBankAccountResult ->
            when (result) {
                is CollectBankAccountResult.Completed -> {
                    val intent = result.response.intent
                    if (intent.status == StripeIntent.Status.RequiresPaymentMethod) {
                        globalResultHandler?.onCanceled()
                        finish()
                    } else if (intent.status == StripeIntent.Status.RequiresConfirmation) {
                        methodId = result.response.intent.paymentMethodId

                        // Confirm the SetupIntent
                        val confirmParams = ConfirmSetupIntentParams.create(
                            clientSecret = setupIntentClientSecret,
                            paymentMethodType = PaymentMethod.Type.USBankAccount
                        )
                        paymentLauncher.confirm(confirmParams)
                    }
                }
                is CollectBankAccountResult.Cancelled -> globalResultHandler?.onCanceled().also { finish() }
                is CollectBankAccountResult.Failed -> globalResultHandler?.onFailed(result.error).also { finish() }
            }
        }

        val s = setupRequest
        if (s != null) {
            setupRequest = null

            setupIntentClientSecret = s.clientSecret

            collectBankAccountLauncher.presentWithSetupIntent(
                publishableKey = publishableKeyInst,
                clientSecret = setupIntentClientSecret,
                configuration = CollectBankAccountConfiguration.USBankAccount(s.name, s.email)
            )
        }
    }

    private fun onPaymentResult(paymentResult: PaymentResult) {
        when (paymentResult) {
            is PaymentResult.Completed -> globalResultHandler?.onCompleted(methodId!!).also { finish() }
            is PaymentResult.Canceled -> globalResultHandler?.onCanceled().also { finish() }
            is PaymentResult.Failed -> globalResultHandler?.onFailed(paymentResult.throwable).also { finish() }
        }
    }
}