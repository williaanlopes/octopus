package com.gurpster.location.data.entity

import com.gurpster.location.domain.model.LocationDomainModel

data class LocationEntity(
    val latitude: Double,
    val longitude: Double
) {
    fun toDomain() = LocationDomainModel(
        latitude = latitude,
        longitude = longitude
    )
}