package com.hectordelgado.celestial.db.entity

import com.hectordelgado.celestial.FavoriteImageOfTheDay
import com.hectordelgado.celestial.network.dto.PictureOfTheDayDto

data class FavoriteImageOfTheDayEntity(
    val id: String,
    val imageUrl: String,
    val explanation: String,
    val dateSaved: String,
    val displayDate: String,
    val copyright: String
) {
    constructor(data: FavoriteImageOfTheDay) : this(
        id = data.id,
        imageUrl = data.image_url,
        explanation = data.explanation,
        dateSaved = data.date_saved,
        displayDate = data.display_date,
        copyright = data.copyright
    )

    constructor(dto: PictureOfTheDayDto) : this(
        id = dto.url,
        imageUrl = dto.url,
        explanation = dto.explanation,
        dateSaved = dto.date,
        displayDate = dto.title,
        copyright = dto.copyright
    )
}