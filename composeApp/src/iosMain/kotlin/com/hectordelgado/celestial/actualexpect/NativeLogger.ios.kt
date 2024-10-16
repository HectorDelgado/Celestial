package com.hectordelgado.celestial.actualexpect

class IosNativeLogger: NativeLogger {
    override fun logDebug(message: String, tag: String?) {
        TODO("Not yet implemented")
    }

    override fun logError(message: String, tag: String?) {
        TODO("Not yet implemented")
    }

}

actual fun getNativeLogger(): NativeLogger = IosNativeLogger()