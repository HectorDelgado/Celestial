package com.hectordelgado.celestial.db

import com.hectordelgado.celestial.db.dao.FavoriteImageOfTheDayDao

class AppDatabase(val favoriteImageOfTheDayDao: FavoriteImageOfTheDayDao) {
    suspend fun clearDatabase() {
        favoriteImageOfTheDayDao.deleteAll()
    }
}
