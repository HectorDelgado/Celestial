package com.hectordelgado.celestial.actualexpect

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AndroidDateFormatter : DateFormatter {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun formatAsDayMonthYear(dateString: String): String {
        val zdt = ZonedDateTime.parse(dateString)
        val formatter = DateTimeFormatter
            .ofPattern("MMMM d, yyyy")
        return zdt.format(formatter)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun formatISO8601AsTimeWithZone(dateString: String): String {
//        val zdt = ZonedDateTime.parse(dateString)
//        val formatter = DateTimeFormatter
//            .ofPattern("HH:mm:ssXXX'['VV']'")
//        return zdt.format(formatter)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCurrentDateAsYYYYMMDD(daysOffset: Long): String {
        val zdt = LocalDate.now().minusDays(daysOffset)
        val formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
        return zdt.format(formatter)
    }
}

actual fun getDateFormatter(): DateFormatter = AndroidDateFormatter()