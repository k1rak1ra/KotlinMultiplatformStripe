package net.k1ra.kotlinmultiplatformstripe

interface CollectBankAccountResultHandler {
    fun onCompleted(paymentMethodId: String)

    fun onCanceled()

    fun onFailed(reason: Throwable)
}