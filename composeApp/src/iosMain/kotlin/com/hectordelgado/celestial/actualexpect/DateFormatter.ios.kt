package com.hectordelgado.celestial.actualexpect

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.addTimeInterval
import platform.Foundation.localeWithLocaleIdentifier
import platform.Foundation.timeZoneForSecondsFromGMT

class IOSDateFormatter : DateFormatter {
    companion object {
        const val SECONDS_IN_A_DAY = 86400
    }
    override fun formatAsDayMonthYear(dateString: String): String {
        val isoFormatter = NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            timeZone = NSTimeZone.timeZoneForSecondsFromGMT(0)
            locale = NSLocale.localeWithLocaleIdentifier("en_US")
        }

        val date = isoFormatter.dateFromString(dateString)!!

        val outputFormatter = NSDateFormatter().apply {
            dateFormat = "MMMM d, yyyy"
            locale = NSLocale.localeWithLocaleIdentifier("en_US")
        }

        return outputFormatter.stringFromDate(date)
    }

    override fun getCurrentDateAsYYYYMMDD(daysOffset: Long): String {
        val currentDate = NSDate()
        val adjustedDate = currentDate.addTimeInterval((daysOffset * -SECONDS_IN_A_DAY).toDouble()) as NSDate
        val formatter = NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
            locale = NSLocale.localeWithLocaleIdentifier("en_US")
        }

        return formatter.stringFromDate(adjustedDate)
    }
}

actual fun getDateFormatter(): DateFormatter = IOSDateFormatter()