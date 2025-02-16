package net.k1ra.kotlinmultiplatformstripe

actual object PaymentConfiguration {
    var stripeInstance: Stripe? = null

    actual fun init(publishableKey: String) {
        stripeInstance = Stripe(publishableKey)
    }
}
external class PaymentIntent : JsAny

external class Elements

external class Stripe(publishableKey: String)
