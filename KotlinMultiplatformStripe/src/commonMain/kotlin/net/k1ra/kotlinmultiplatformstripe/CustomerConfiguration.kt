package net.k1ra.kotlinmultiplatformstripe

data class CustomerConfiguration internal constructor(
    val id: String,
    val ephemeralKeySecret: String,
    internal val accessType: CustomerAccessType,
) {
    constructor(
        id: String,
        ephemeralKeySecret: String,
    ) : this(
        id = id,
        ephemeralKeySecret = ephemeralKeySecret,
        accessType = CustomerAccessType.LegacyCustomerEphemeralKey(ephemeralKeySecret)
    )

    companion object {
        fun createWithCustomerSession(
            id: String,
            clientSecret: String
        ): CustomerConfiguration {
            return CustomerConfiguration(
                id = id,
                ephemeralKeySecret = "",
                accessType = CustomerAccessType.CustomerSession(clientSecret)
            )
        }
    }
}