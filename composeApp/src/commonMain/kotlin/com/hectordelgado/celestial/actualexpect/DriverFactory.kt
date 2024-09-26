package com.hectordelgado.celestial.actualexpect

import app.cash.sqldelight.db.SqlDriver
import com.hectordelgado.celestial.SqlDelightDatabase

//expect fun getSqlDriver(): SqlDriver

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): SqlDelightDatabase {
    val driver = driverFactory.createDriver()
    val database = SqlDelightDatabase(driver)

    return database
}

