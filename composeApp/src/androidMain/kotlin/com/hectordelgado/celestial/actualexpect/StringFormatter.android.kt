package com.hectordelgado.celestial.actualexpect

class AndroidStringFormatter : StringFormatter {
    override fun format(string: String, vararg args: Any): String {
        return String.format(string, args)
    }

}

actual fun getStringFormatter(): StringFormatter = AndroidStringFormatter()