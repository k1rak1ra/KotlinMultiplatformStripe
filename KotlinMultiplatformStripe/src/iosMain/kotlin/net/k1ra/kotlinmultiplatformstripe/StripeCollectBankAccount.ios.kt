package net.k1ra.kotlinmultiplatformstripe

import StripeIosBridge.BankAccountSetup
import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIViewController

actual class StripeCollectBankAccount(
    val viewController: UIViewController?,
)

@Composable
actual fun rememberStripeCollectBankAccount(): StripeCollectBankAccount {
    return StripeCollectBankAccount(getCurrentViewController())
}

@OptIn(ExperimentalForeignApi::class)
actual fun showBankAccountSetup(resultHandler: CollectBankAccountResultHandler, stripeCollectBankAccount: StripeCollectBankAccount, request: CollectBankAccountSetupRequest) {
    BankAccountSetup().showWithViewController(
        stripeCollectBankAccount.viewController!!,
        request.name,
        request.email,
        request.clientSecret,
        StripeIosReturnLink.link,
        { resultHandler.onCompleted(it!!) },
        { resultHandler.onCanceled() },
        { resultHandler.onFailed(Exception(it!!.description)) }
    )
}