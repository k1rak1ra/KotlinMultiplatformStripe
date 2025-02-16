package net.k1ra.kotlinmultiplatformstripe

import StripeIosBridge.StripeCustomerConfiguration
import StripeIosBridge.StripeMerchantConfiguration
import StripeIosBridge.StupidBridge
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow

actual class PaymentSheet(
    val viewController: UIViewController?,
    val resultHandler: PaymentSheetResultHandler
)

fun getCurrentViewController(): UIViewController? {
    val keyWindow = UIApplication.sharedApplication.keyWindow
    var rootViewController = keyWindow?.rootViewController
    while (rootViewController?.presentedViewController != null) {
        rootViewController = rootViewController.presentedViewController
    }
    return rootViewController
}

@Composable
actual fun rememberPaymentSheet(resultHandler: PaymentSheetResultHandler): PaymentSheet {

    return PaymentSheet(getCurrentViewController(), resultHandler)
}

@OptIn(ExperimentalForeignApi::class)
actual fun presentStripePaymentSheet(
    sheet: PaymentSheet,
    merchantConfig: MerchantConfiguration,
    paymentIntentClientSecret: String,
    customerConfig: CustomerConfiguration?,
) {
    val stupidMerchantConfig = StripeMerchantConfiguration(merchantConfig.merchantDisplayName, merchantConfig.allowsDelayedPaymentMethods)

    val stupidCustomerConfig = if (customerConfig != null)
      StripeCustomerConfiguration(customerConfig.id, customerConfig.ephemeralKeySecret)
    else
        null

    StupidBridge().presentPaymentSheetWithMerchantConfig(
        merchantConfig = stupidMerchantConfig,
        customerConfig = stupidCustomerConfig,
        paymentIntentClientSecret = paymentIntentClientSecret,
        viewController = sheet.viewController!!,
        completed = { sheet.resultHandler.onCompleted() },
        canceled = { sheet.resultHandler.onCanceled() },
        failed = { sheet.resultHandler.onFailed(Exception(it?.description)) }
    )
}