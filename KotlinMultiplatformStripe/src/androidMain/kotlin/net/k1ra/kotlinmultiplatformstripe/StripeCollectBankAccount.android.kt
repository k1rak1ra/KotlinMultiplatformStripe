package net.k1ra.kotlinmultiplatformstripe

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.stripe.android.model.ConfirmSetupIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.StripeIntent
import com.stripe.android.payments.bankaccount.CollectBankAccountLauncher
import com.stripe.android.payments.bankaccount.navigation.CollectBankAccountResult
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult

actual class StripeCollectBankAccount(val activity: Activity)

@Composable
actual fun rememberStripeCollectBankAccount(): StripeCollectBankAccount {
    return StripeCollectBankAccount(LocalContext.current.getActivity())
}

fun Context.getActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }

    throw Exception("Could not get Activity")
}

actual fun showBankAccountSetup(resultHandler: CollectBankAccountResultHandler, stripeCollectBankAccount: StripeCollectBankAccount, request: CollectBankAccountSetupRequest) {
    setupRequest = request
    globalResultHandler = resultHandler

    val intent = Intent(stripeCollectBankAccount.activity, BankAccountLinkActivity::class.java)

    stripeCollectBankAccount.activity.startActivity(intent)
}