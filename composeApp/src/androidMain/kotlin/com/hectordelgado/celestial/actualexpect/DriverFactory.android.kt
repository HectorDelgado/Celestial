package com.hectordelgado.celestial.actualexpect

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hectordelgado.celestial.SqlDelightDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SqlDelightDatabase.Schema, context, "temp-test.db")
    }
}