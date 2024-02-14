package com.gurpster.location.domain.model

import com.gurpster.location.view.model.LocationModel

data class LocationDomainModel(
    val latitude: Double,
    val longitude: Double
) {
    fun toModel() = LocationModel(
        longitude = latitude,
        latitude = longitude
    )
}
