import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import net.k1ra.kotlinmultiplatformstripe.CollectBankAccountResultHandler
import net.k1ra.kotlinmultiplatformstripe.CollectBankAccountSetupRequest
import net.k1ra.kotlinmultiplatformstripe.MerchantConfiguration
import net.k1ra.kotlinmultiplatformstripe.PaymentConfiguration
import net.k1ra.kotlinmultiplatformstripe.PaymentSheetResultHandler
import net.k1ra.kotlinmultiplatformstripe.presentStripePaymentSheet
import net.k1ra.kotlinmultiplatformstripe.rememberPaymentSheet
import net.k1ra.kotlinmultiplatformstripe.rememberStripeCollectBankAccount
import net.k1ra.kotlinmultiplatformstripe.showBankAccountSetup
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize())
        {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                val paymentSheet = rememberPaymentSheet(object : PaymentSheetResultHandler {
                    override fun onCompleted() {
                        println("TRANSACTION COMPLETED")
                    }

                    override fun onCanceled() {
                        println("TRANSACTION CANCELLED")
                    }

                    override fun onFailed(reason: Throwable) {
                        reason.printStackTrace()
                        println("TRANSACTION FAILED")
                    }
                })

                val collectBankAccount = rememberStripeCollectBankAccount()
                val collectBankAccountHandler = object: CollectBankAccountResultHandler {
                    override fun onCompleted(paymentMethodId: String) {
                        println("BANK ACCOUNT ENTRY SUCCESS $paymentMethodId")
                    }

                    override fun onCanceled() {
                        println("BANK ACCOUNT ENTRY CANCELLED")
                    }

                    override fun onFailed(reason: Throwable) {
                        reason.printStackTrace()
                        println("BANK ACCOUNT ENTRY FAILED")
                    }

                }

                val merchantConfig = MerchantConfiguration(
                    "Test Merchant",
                    true
                )

                PaymentConfiguration.init("")
                val paymentIntentClientSecret = "YOUR_PI"
                val bankAccountSetupRequest = CollectBankAccountSetupRequest(
                    "",
                    "Test User",
                    "test@example.com"
                )

                Button(
                    onClick = {
                        presentStripePaymentSheet(
                            paymentSheet,
                            merchantConfig,
                            paymentIntentClientSecret
                        )
                    }
                ) {
                    Text("Show payment sheet")
                }

                Button(
                    onClick = {
                        showBankAccountSetup(collectBankAccountHandler, collectBankAccount, bankAccountSetupRequest)
                    }
                ) {
                    Text("Show bank account setup")
                }
            }
        }
    }
}