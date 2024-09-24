package com.hectordelgado.celestial.actualexpect

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val sqlDriverModule: Module = module {
    single<SqlDriver> { DriverFactory().createDriver() }
}