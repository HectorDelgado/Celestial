package com.hectordelgado.celestial.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class MarsPhotosDto(
    val photos: List<MarsPhoto>
) {
    enum class Rover(val value: String) {
        CURIOSITY("curiosity"),
        OPPORTUNITY("opportunity"),
        SPIRIT("spirit")
    }
}

@Serializable
data class MarsPhoto(
    val id: Int,
    val sol: Int,
    val camera: Camera,
    val img_src: String,
    val earth_date: String,
    val rover: Rover
)

@Serializable
data class Camera(
    val id: Int,
    val name: String,
    val rover_id: Int,
    val full_name: String
)

@Serializable
data class Rover(
    val id: Int,
    val name: String,
    val landing_date: String,
    val launch_date: String,
    val status: String,
    val max_sol: Int,
    val max_date: String,
    val total_photos: Int,
    val cameras: List<RoverCamera>
)

@Serializable
data class RoverCamera(
    val name: String,
    val full_name: String
)
