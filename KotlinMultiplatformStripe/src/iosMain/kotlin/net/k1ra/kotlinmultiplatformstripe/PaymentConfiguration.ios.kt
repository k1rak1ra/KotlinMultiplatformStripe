package net.k1ra.kotlinmultiplatformstripe

import StripeIosBridge.StupidBridge
import kotlinx.cinterop.ExperimentalForeignApi

actual object PaymentConfiguration {
    @OptIn(ExperimentalForeignApi::class)
    actual fun init(publishableKey: String) {
        StupidBridge().setPublishableKeyWithPublishableKey(publishableKey)
    }
}