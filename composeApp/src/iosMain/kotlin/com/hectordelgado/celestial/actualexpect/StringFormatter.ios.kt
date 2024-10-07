package com.hectordelgado.celestial.actualexpect

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

class IOSStringFormatter : StringFormatter {
    override fun format(string: String, vararg args: Any): String {
        return NSString.stringWithFormat(string, args)
    }
}

actual fun getStringFormatter(): StringFormatter = IOSStringFormatter()