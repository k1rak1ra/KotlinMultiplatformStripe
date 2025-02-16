package net.k1ra.kotlinmultiplatformstripe

interface PaymentSheetResultHandler {
    fun onCompleted()

    fun onCanceled()

    fun onFailed(reason: Throwable)
}