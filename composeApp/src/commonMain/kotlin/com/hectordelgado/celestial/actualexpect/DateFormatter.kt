package com.hectordelgado.celestial.actualexpect

interface DateFormatter {
    fun formatAsDayMonthYear(dateString: String): String

    fun getDateAsYYYYMMDD(daysOffset: Long = 0L): String
}

expect fun getDateFormatter(): DateFormatter