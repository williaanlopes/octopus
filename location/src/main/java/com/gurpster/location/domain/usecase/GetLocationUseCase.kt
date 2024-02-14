package com.gurpster.location.domain.usecase

import com.gurpster.location.domain.model.LocationDomainModel
import com.gurpster.location.domain.repository.LocationRepository
import io.reactivex.rxjava3.core.Flowable

class GetLocationUseCase(
    private val locationRepository: LocationRepository
) {

    fun build(): Flowable<LocationDomainModel> {
        return locationRepository.getLocation()
    }

}