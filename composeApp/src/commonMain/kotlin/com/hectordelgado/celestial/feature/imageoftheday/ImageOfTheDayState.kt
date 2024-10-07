package com.hectordelgado.celestial.feature.imageoftheday

import com.hectordelgado.celestial.db.entity.FavoriteImageOfTheDayEntity
import com.hectordelgado.celestial.network.response.PictureOfTheDayResponse

data class ImageOfTheDayState(
    val daysOffset: Long,
    val imageOfTheDay: ImageOfTheDay?,
    val favorites: List<ImageOfTheDay>
) {
    companion object {
        val empty = ImageOfTheDayState(
            daysOffset = 0L,
            imageOfTheDay = null,
            favorites = emptyList()
        )
    }
}

data class ImageOfTheDay(
    val id: String,
    val title: String,
    val imageUrl: String,
    val explanation: String,
    val displayDate: String,
    val copyright: String,
    val isSaved: Boolean
) {
    constructor(entity: FavoriteImageOfTheDayEntity) : this(
        id = entity.id,
        title = entity.title,
        imageUrl = entity.imageUrl,
        explanation = entity.explanation,
        displayDate = entity.displayDate,
        copyright = entity.copyright,
        isSaved = true
    )

    constructor(dto: PictureOfTheDayResponse) : this(
        id = dto.url,
        title = dto.title,
        imageUrl = dto.url,
        explanation = dto.explanation,
        displayDate = dto.title,
        copyright = dto.copyright ?: "",
        isSaved = false
    )
}