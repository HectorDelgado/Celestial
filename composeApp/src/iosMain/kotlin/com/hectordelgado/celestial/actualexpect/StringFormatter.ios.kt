package com.hectordelgado.celestial.actualexpect

class IOSStringFormatter : StringFormatter {
    override fun format(string: String, args: Any): String {
        return string
    }
}

actual fun getStringFormatter(): StringFormatter = IOSStringFormatter()