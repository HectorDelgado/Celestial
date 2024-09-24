package com.hectordelgado.celestial.actualexpect

interface MLogger {
    fun logDebug(message: String, tag: String? = "logz")
    fun logError(message: String, tag: String? = "logz")
}

expect fun getMLogger(): MLogger