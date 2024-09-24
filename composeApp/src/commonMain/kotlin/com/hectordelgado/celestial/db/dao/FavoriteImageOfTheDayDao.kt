package com.hectordelgado.celestial.db.dao

import com.hectordelgado.celestial.SqlDelightDatabase
import com.hectordelgado.celestial.db.entity.FavoriteImageOfTheDayEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FavoriteImageOfTheDayDao {
    suspend fun selectAll(): List<FavoriteImageOfTheDayEntity>
    suspend fun insert(item: FavoriteImageOfTheDayEntity)
    suspend fun delete(id: String)
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

    override suspend fun insert(item: FavoriteImageOfTheDayEntity){
        return database.favoriteImageOfTheDayQueries
            .insert(
                id = item.id,
                image_url = item.imageUrl,
                explanation = item.explanation,
                date_saved = item.dateSaved,
                display_date = item.displayDate,
                copyright = item.copyright
            )
    }

    override suspend fun delete(id: String) {
        return database.favoriteImageOfTheDayQueries
            .delete(id)
    }
}
