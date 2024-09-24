package com.hectordelgado.celestial.di

import com.hectordelgado.celestial.SqlDelightDatabase
import com.hectordelgado.celestial.actualexpect.createDatabase
import com.hectordelgado.celestial.actualexpect.sqlDriverModule
import com.hectordelgado.celestial.data.datasource.DefaultNasaRepository
import com.hectordelgado.celestial.data.datasource.NasaRepository
import com.hectordelgado.celestial.db.AppDatabase
import com.hectordelgado.celestial.db.DefaultAppDatabase
import com.hectordelgado.celestial.db.dao.DefaultFavoriteImageOfTheDayDao
import com.hectordelgado.celestial.db.dao.FavoriteImageOfTheDayDao
import com.hectordelgado.celestial.feature.imageoftheday.ImageOfTheDayScreen
import com.hectordelgado.celestial.feature.imageoftheday.ImageOfTheDayScreenModel
import com.hectordelgado.celestial.feature.solarflare.SolarFlareScreen
import com.hectordelgado.celestial.feature.solarflare.SolarFlareScreenModel
import com.hectordelgado.celestial.feature.splash.SplashScreen
import com.hectordelgado.celestial.feature.splash.SplashScreenModel
import com.hectordelgado.celestial.network.api.NasaApi
import com.hectordelgado.celestial.network.api.NetworkManager
import org.koin.dsl.module

val dataModule = module {
    factory<NasaRepository> { DefaultNasaRepository(get()) }
}

val dbModule = module {
    single<SqlDelightDatabase> { createDatabase(get()) }
    single<FavoriteImageOfTheDayDao> { DefaultFavoriteImageOfTheDayDao(get()) }
    single<AppDatabase> { DefaultAppDatabase(get())}
}

val networkModule = module {
    factory { NasaApi(get()) }
    single { NetworkManager() }
}

val featureModule = module {
    // solar flare module
    factory { SolarFlareScreen() }
    single { SolarFlareScreenModel(get()) }

    // splash module
    factory { SplashScreen() }
    single { SplashScreenModel() }

    // image of the day module
    factory { ImageOfTheDayScreen() }
    single { ImageOfTheDayScreenModel(get(), get()) }
}

fun sharedModule() = listOf(
    sqlDriverModule
)

fun appModule() = listOf(
    dataModule,
    dbModule,
    featureModule,
    networkModule
)


