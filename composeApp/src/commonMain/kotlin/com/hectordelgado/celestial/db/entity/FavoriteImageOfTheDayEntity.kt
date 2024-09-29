package com.hectordelgado.celestial.db.entity

import com.hectordelgado.celestial.FavoriteImageOfTheDay
import com.hectordelgado.celestial.feature.imageoftheday.ImageOfTheDay

data class FavoriteImageOfTheDayEntity(
    val id: String,
    val title: String,
    val imageUrl: String,
    val explanation: String,
    val displayDate: String,
    val copyright: String
) {
    constructor(data: FavoriteImageOfTheDay) : this(
        id = data.id,
        title = data.title,
        imageUrl = data.image_url,
        explanation = data.explanation,
        displayDate = data.display_date,
        copyright = data.copyright
    )

    constructor(model: ImageOfTheDay) : this(
        id = model.id,
        title = model.title,
        imageUrl = model.imageUrl,
        explanation = model.explanation,
        displayDate = model.displayDate,
        copyright = model.copyright
    )
}