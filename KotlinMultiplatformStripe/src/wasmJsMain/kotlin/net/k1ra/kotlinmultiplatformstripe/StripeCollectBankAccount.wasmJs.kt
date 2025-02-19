package net.k1ra.kotlinmultiplatformstripe

import androidx.compose.runtime.Composable

actual class StripeCollectBankAccount

@Composable
actual fun rememberStripeCollectBankAccount(): StripeCollectBankAccount {
    return StripeCollectBankAccount()
}

actual fun showBankAccountSetup(resultHandler: CollectBankAccountResultHandler, stripeCollectBankAccount: StripeCollectBankAccount, request: CollectBankAccountSetupRequest) {
    showBankAccountEntry(
        PaymentConfiguration.stripeInstance!!,
        request.clientSecret,
        request.name,
        request.email,
        { resultHandler.onCompleted(it) },
        { resultHandler.onCanceled() },
        { resultHandler.onFailed(Exception(it)) }
    )
}

fun showBankAccountEntry(
    stripe: Stripe,
    clientSecret: String,
    name: String,
    email: String,
    onSuccess: (String) -> Unit,
    onCancelled: () -> Unit,
    onFailed: (String) -> Unit
) {
    js("""
  stripe.collectBankAccountForSetup({
    clientSecret,
    params: {
      payment_method_type: 'us_bank_account',
      payment_method_data: {
        billing_details: {
          // name is required
          name: name,
          email: email
        },
      },
    },
    // Optional, helpful for client-side logic
    expand: ['payment_method']
  }).then(({setupIntent, error}) => {
    if (error) {
      // Inform your user that there was an error.
      onFailed(error.message);
    } else {
      if (setupIntent.status === "requires_confirmation") {
        // Your user provided bank account details

        // When you pass `expand: ['payment_method']` to `collectBankAccountForSetup`,
        // `setupIntent.payment_method` has data about the Payment Method, including
        // the Financial Connections Account ID, if your user used Financial Connections
        // to provide their bank account details.
        const usedFinancialConnections = typeof setupIntent.payment_method.financial_connections_account === 'string';
        
        var pmid = setupIntent.payment_method.id;
        
        return stripe.confirmUsBankAccountSetup(clientSecret).then(({setupIntent, error}) => {
          if (error) {
            // Inform your user that there was an error, check your request logs for errors
            onFailed(error.message);
          } else {
            if (setupIntent.status === 'succeeded') {
              onSuccess(pmid);
            } else {
              onCancelled();
              // Unable to confirm the SetupIntent, re-fetch the SetupIntent for details
            }
          }
        });
      } else {
        onCancelled();
        return null;
      }
    }
  });
    """)
}

