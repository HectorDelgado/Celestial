package com.hectordelgado.celestial.db

import com.hectordelgado.celestial.db.dao.FavoriteImageOfTheDayDao

interface AppDatabase {
    val favoriteImageOfTheDayDao: FavoriteImageOfTheDayDao
}

class DefaultAppDatabase(
    override val favoriteImageOfTheDayDao: FavoriteImageOfTheDayDao
) : AppDatabase


