package com.gurpster.location.data.repository

import com.gurpster.location.data.source.LocationDataSource
import com.gurpster.location.domain.model.AddressDomainModel
import com.gurpster.location.domain.model.LocationDomainModel
import com.gurpster.location.domain.repository.LocationRepository
import io.reactivex.rxjava3.core.Flowable

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource
) : LocationRepository {

    override fun getLocation(): Flowable<LocationDomainModel> {
        return locationDataSource
            .locationObservable
            .map { it.toDomain() }
    }

    override fun getAddress(): Flowable<AddressDomainModel> {
        return locationDataSource
            .addressObservable
            .map { it.toDomain() }
    }

}