package com.hectordelgado.celestial.feature.imageoftheday

import com.hectordelgado.celestial.db.entity.FavoriteImageOfTheDayEntity
import com.hectordelgado.celestial.network.dto.PictureOfTheDayDto

data class ImageOfTheDayState(
    val daysOffset: Long,
    val pictureOfTheDayDto: PictureOfTheDayDto?,
    val favorites: List<FavoriteImageOfTheDayEntity>
) {
    companion object {
        val empty = ImageOfTheDayState(
            daysOffset = 0L,
            pictureOfTheDayDto = null,
            favorites = emptyList()
        )
    }
}