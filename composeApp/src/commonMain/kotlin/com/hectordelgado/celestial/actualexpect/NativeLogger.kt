package com.hectordelgado.celestial.actualexpect

interface NativeLogger {
    fun logDebug(message: String, tag: String? = "logz")
    fun logError(message: String, tag: String? = "logz")
}

expect fun getNativeLogger(): NativeLogger