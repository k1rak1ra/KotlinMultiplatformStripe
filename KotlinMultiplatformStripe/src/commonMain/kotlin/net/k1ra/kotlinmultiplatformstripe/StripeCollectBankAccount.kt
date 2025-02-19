package net.k1ra.kotlinmultiplatformstripe

import androidx.compose.runtime.Composable

expect class StripeCollectBankAccount

@Composable
expect fun rememberStripeCollectBankAccount() : StripeCollectBankAccount

expect fun showBankAccountSetup(resultHandler: CollectBankAccountResultHandler, stripeCollectBankAccount: StripeCollectBankAccount, request: CollectBankAccountSetupRequest)