package com.gurpster.location.domain.model

import com.gurpster.location.data.entity.AddressEntity

data class AddressDomainModel(val city: String) {
    fun toEntity() = AddressEntity(
        city = city
    )

}
