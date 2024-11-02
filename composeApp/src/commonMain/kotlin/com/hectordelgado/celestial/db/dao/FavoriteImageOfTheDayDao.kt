package com.hectordelgado.celestial.db.dao

import com.hectordelgado.celestial.SqlDelightDatabase
import com.hectordelgado.celestial.db.entity.FavoriteImageOfTheDayEntity

interface FavoriteImageOfTheDayDao {
    suspend fun selectAll(): List<FavoriteImageOfTheDayEntity>
    suspend fun insert(item: FavoriteImageOfTheDayEntity)
    suspend fun delete(id: String)
    suspend fun deleteAll()
}

class DefaultFavoriteImageOfTheDayDao(
    private val database: SqlDelightDatabase
) : FavoriteImageOfTheDayDao {
    override suspend fun selectAll(): List<FavoriteImageOfTheDayEntity> {
        return database.favoriteImageOfTheDayQueries
            .selectAll()
            .executeAsList()
            .map { FavoriteImageOfTheDayEntity(it) }
    }

    override suspend fun insert(item: FavoriteImageOfTheDayEntity) {
        return database.favoriteImageOfTheDayQueries
            .insert(
                id = item.id,
                title = item.title,
                image_url = item.imageUrl,
                explanation = item.explanation,
                display_date = item.displayDate,
                media_type = item.mediaType,
                copyright = item.copyright ?: ""
            )
    }

    override suspend fun delete(id: String) {
        return database.favoriteImageOfTheDayQueries
            .delete(id)
    }

    override suspend fun deleteAll() {
        database.favoriteImageOfTheDayQueries
            .deleteAll()
    }
}
