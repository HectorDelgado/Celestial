package com.hectordelgado.celestial.db

import com.hectordelgado.celestial.db.dao.FavoriteImageOfTheDayDao

class DefaultAppDatabase(override val favoriteImageOfTheDayDao: FavoriteImageOfTheDayDao) : AppDatabase {
    override suspend fun clearDatabase() {
        favoriteImageOfTheDayDao.deleteAll()
    }
}

interface AppDatabase {
    val favoriteImageOfTheDayDao: FavoriteImageOfTheDayDao

    suspend fun clearDatabase()
}