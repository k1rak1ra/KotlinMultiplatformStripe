package net.k1ra.kotlinmultiplatformstripe

import androidx.compose.runtime.Composable

actual class StripeCollectBankAccount

@Composable
actual fun rememberStripeCollectBankAccount(): StripeCollectBankAccount {
    return StripeCollectBankAccount()
}

actual fun showBankAccountSetup(resultHandler: CollectBankAccountResultHandler, stripeCollectBankAccount: StripeCollectBankAccount, request: CollectBankAccountSetupRequest) {
}