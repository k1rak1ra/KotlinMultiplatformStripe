package net.k1ra.kotlinmultiplatformstripe

import androidx.compose.runtime.Composable

actual class PaymentSheet

@Composable
actual fun rememberPaymentSheet(resultHandler: PaymentSheetResultHandler): PaymentSheet {
    return PaymentSheet()
}

actual fun presentStripePaymentSheet(
    sheet: PaymentSheet,
    merchantConfig: MerchantConfiguration,
    paymentIntentClientSecret: String,
    customerConfig: CustomerConfiguration?,
) {
}