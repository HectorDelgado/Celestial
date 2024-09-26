package com.hectordelgado.celestial.actualexpect

class IOSDateFormatter : DateFormatter {
    override fun formatAsDayMonthYear(dateString: String): String {
        return dateString
    }

    override fun formatISO8601AsTimeWithZone(dateString: String): String {
        return ""
    }

    override fun getCurrentDateAsYYYYMMDD(daysOffset: Long): String {
        return ""
    }
}

actual fun getDateFormatter(): DateFormatter = IOSDateFormatter()