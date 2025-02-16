package net.k1ra.kotlinmultiplatformstripe

import androidx.compose.runtime.Composable


expect class PaymentSheet

@Composable
expect fun rememberPaymentSheet(resultHandler: PaymentSheetResultHandler) : PaymentSheet

expect fun presentStripePaymentSheet(
    sheet: PaymentSheet,
    merchantConfig: MerchantConfiguration,
    paymentIntentClientSecret: String,
    customerConfig: CustomerConfiguration? = null,
)