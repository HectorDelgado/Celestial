package com.hectordelgado.celestial.actualexpect

interface DateFormatter {
    /**
     * Formats an ISO 8601 date string in the format "MMMM d, yyyy" (Ex: January 1, 2024).
     *
     * @param dateString the ISO 8601 date string to format.
     */
    fun formatAsDayMonthYear(dateString: String): String

    /**
     * Returns the current date as a string in the format YYYY-MM-DD.
     *
     * @param daysOffset the number of days to subtract from the current date.
     */
    fun getCurrentDateAsYYYYMMDD(daysOffset: Long = 0L): String
}

expect fun getDateFormatter(): DateFormatter