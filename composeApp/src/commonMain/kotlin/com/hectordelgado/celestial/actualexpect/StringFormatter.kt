package com.hectordelgado.celestial.actualexpect

interface StringFormatter {
    fun format(string: String, args: Any): String
}

expect fun getStringFormatter(): StringFormatter