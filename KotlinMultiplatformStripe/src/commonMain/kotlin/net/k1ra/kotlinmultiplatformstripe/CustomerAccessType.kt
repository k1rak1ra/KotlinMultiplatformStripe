package net.k1ra.kotlinmultiplatformstripe

internal sealed interface CustomerAccessType  {
    val analyticsValue: String

    data class LegacyCustomerEphemeralKey(val ephemeralKeySecret: String) : CustomerAccessType {
        override val analyticsValue: String = "legacy"
    }

    data class CustomerSession(val customerSessionClientSecret: String) : CustomerAccessType {
        override val analyticsValue: String = "customer_session"
    }
}