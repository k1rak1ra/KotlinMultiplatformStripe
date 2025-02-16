package net.k1ra.kotlinmultiplatformstripe

import androidx.compose.runtime.Composable

actual class PaymentSheet

@Composable
actual fun rememberPaymentSheet(resultHandler: PaymentSheetResultHandler): PaymentSheet {
    TODO("Not yet implemented")
}

actual fun presentStripePaymentSheet(
    sheet: PaymentSheet,
    merchantConfig: MerchantConfiguration,
    paymentIntentClientSecret: String,
    customerConfig: CustomerConfiguration?,
) {
}