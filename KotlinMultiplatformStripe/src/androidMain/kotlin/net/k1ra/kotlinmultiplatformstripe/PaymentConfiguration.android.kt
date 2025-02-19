package net.k1ra.kotlinmultiplatformstripe

import com.stripe.android.PaymentConfiguration
import net.k1ra.sharedprefkmm.SharedPrefKmmInitContentProvider

var publishableKeyInst = ""

actual object PaymentConfiguration {
    actual fun init(publishableKey: String) {
        publishableKeyInst = publishableKey
        PaymentConfiguration.init(SharedPrefKmmInitContentProvider.appContext, publishableKey)
    }
}