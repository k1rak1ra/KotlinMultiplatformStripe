package net.k1ra.kotlinmultiplatformstripe

import com.stripe.android.PaymentConfiguration
import net.k1ra.sharedprefkmm.SharedPrefKmmInitContentProvider

actual object PaymentConfiguration {
    actual fun init(publishableKey: String) {
        PaymentConfiguration.init(SharedPrefKmmInitContentProvider.appContext, publishableKey)
    }
}