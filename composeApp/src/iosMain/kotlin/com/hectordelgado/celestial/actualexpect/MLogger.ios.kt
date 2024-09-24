package com.hectordelgado.celestial.actualexpect

class IOSMLogger: MLogger {
    override fun logDebug(message: String, tag: String?) {
        TODO("Not yet implemented")
    }

    override fun logError(message: String, tag: String?) {
        TODO("Not yet implemented")
    }

}

actual fun getMLogger(): MLogger = IOSMLogger()