package com.hectordelgado.celestial.actualexpect

import android.util.Log

class AndroidMLogger() : MLogger {
    override fun logDebug(message: String, tag: String?) {
        Log.d(tag, message, null)
    }

    override fun logError(message: String, tag: String?) {
        Log.e(tag, message, null)
    }

}

actual fun getMLogger(): MLogger = AndroidMLogger()