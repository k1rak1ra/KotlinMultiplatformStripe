package net.k1ra.kotlinmultiplatformstripe

data class CollectBankAccountSetupRequest(
    val clientSecret: String,
    val name: String,
    val email: String
)