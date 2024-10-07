    package com.hectordelgado.celestial.actualexpect

interface StringFormatter {
    fun format(string: String, vararg args: Any): String
}

expect fun getStringFormatter(): StringFormatter