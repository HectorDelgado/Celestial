package com.hectordelgado.celestial.actualexpect

interface DateFormatter {
    fun formatAsDayMonthYear(dateString: String): String

    fun formatISO8601AsTimeWithZone(dateString: String): String

    fun getCurrentDateAsYYYYMMDD(daysOffset: Long = 0L): String
}

expect fun getDateFormatter(): DateFormatter