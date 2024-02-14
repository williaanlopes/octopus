package com.gurpster.location.data.entity

import com.gurpster.location.domain.model.AddressDomainModel
import com.gurpster.location.domain.model.LocationDomainModel

data class AddressEntity(
    val city: String
) {
    fun toDomain() = AddressDomainModel(
        city = city
    )
}