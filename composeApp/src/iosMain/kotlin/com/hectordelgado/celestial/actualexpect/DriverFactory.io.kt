package com.hectordelgado.celestial.actualexpect

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.hectordelgado.celestial.SqlDelightDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(SqlDelightDatabase.Schema, "test.db")
    }
}