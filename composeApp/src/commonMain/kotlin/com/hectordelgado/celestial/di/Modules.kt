package com.hectordelgado.celestial.di

import com.hectordelgado.celestial.SqlDelightDatabase
import com.hectordelgado.celestial.actualexpect.createDatabase
import com.hectordelgado.celestial.actualexpect.sqlDriverModule
import com.hectordelgado.celestial.data.DefaultNasaRepository
import com.hectordelgado.celestial.data.NasaRepository
import com.hectordelgado.celestial.db.AppDatabase
import com.hectordelgado.celestial.db.DefaultAppDatabase
import com.hectordelgado.celestial.db.dao.DefaultFavoriteImageOfTheDayDao
import com.hectordelgado.celestial.db.dao.FavoriteImageOfTheDayDao
import com.hectordelgado.celestial.feature.imageoftheday.ImageOfTheDayScreen
import com.hectordelgado.celestial.feature.imageoftheday.ImageOfTheDayScreenModel
import com.hectordelgado.celestial.feature.marsphotos.MarsPhotoScreenModel
import com.hectordelgado.celestial.feature.marsphotos.MarsPhotosScreen
import com.hectordelgado.celestial.feature.home.HomeScreen
import com.hectordelgado.celestial.feature.home.HomeScreenModel
import com.hectordelgado.celestial.feature.imageoftheday.FavoriteImagesOfTheDayScreen
import com.hectordelgado.celestial.feature.imageoftheday.FavoriteImagesOfTheDayScreenModel
import com.hectordelgado.celestial.network.api.NasaApi
import com.hectordelgado.celestial.network.NetworkManager
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
    // image of the day module
    factory { ImageOfTheDayScreen() }
    single { ImageOfTheDayScreenModel(get(), get()) }
    factory { FavoriteImagesOfTheDayScreen() }
    single { FavoriteImagesOfTheDayScreenModel(get()) }

    // mars photos module
    factory { MarsPhotosScreen() }
    single { MarsPhotoScreenModel(get()) }

    // splash module
    factory { HomeScreen() }
    single { HomeScreenModel() }


}

fun platformModules() = listOf(
    sqlDriverModule
)

fun appModule() = listOf(
    dataModule,
    dbModule,
    featureModule,
    networkModule
)


