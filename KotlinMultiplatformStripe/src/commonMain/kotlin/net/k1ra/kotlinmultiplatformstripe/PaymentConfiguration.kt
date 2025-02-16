package net.k1ra.kotlinmultiplatformstripe

expect object PaymentConfiguration {
    fun init(publishableKey: String)
}