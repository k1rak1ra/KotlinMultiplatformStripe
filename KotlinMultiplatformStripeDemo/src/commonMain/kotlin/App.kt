import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import net.k1ra.kotlinmultiplatformstripe.MerchantConfiguration
import net.k1ra.kotlinmultiplatformstripe.PaymentConfiguration
import net.k1ra.kotlinmultiplatformstripe.PaymentSheetResultHandler
import net.k1ra.kotlinmultiplatformstripe.presentStripePaymentSheet
import net.k1ra.kotlinmultiplatformstripe.rememberPaymentSheet
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

                val merchantConfig = MerchantConfiguration(
                    "Test Merchant",
                    true
                )

                PaymentConfiguration.init("pk_test_51QVMQMG27rpSTHjUsC5L87a50NLREZ8NPo7ui3X1lhj01hAmvBzCVyEkrChdw1IXfRnWeYWViG6Gvd36TtKVg0Jb00E2niYvzX")
                val paymentIntentClientSecret = "pi_3QnN8eG27rpSTHjU1fkcIkgx_secret_RFQEkpCmfARLJ9LyC9OQ2nj3b"

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
            }
        }
    }
}