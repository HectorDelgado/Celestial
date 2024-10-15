package com.hectordelgado.celestial.feature.imageoftheday

data class FavoriteImagesOfTheDayState(
    val previews: List<ImageOfTheDay>,
    val selectedPreview: ImageOfTheDay?
) {
    companion object {
        val empty = FavoriteImagesOfTheDayState(previews = emptyList(), selectedPreview = null)
    }
}
