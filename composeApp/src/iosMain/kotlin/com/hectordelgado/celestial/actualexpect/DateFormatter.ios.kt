package com.hectordelgado.celestial.actualexpect

class IOSDateFormatter : DateFormatter {
    override fun formatAsDayMonthYear(dateString: String): String {
        return dateString
    }

    override fun getDateAsYYYYMMDD(daysOffset: Long): String {
        return ""
    }
}

actual fun getDateFormatter(): DateFormatter = IOSDateFormatter()