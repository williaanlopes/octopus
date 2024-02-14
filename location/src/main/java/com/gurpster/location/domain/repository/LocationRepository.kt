package com.gurpster.location.domain.repository

import com.gurpster.location.domain.model.AddressDomainModel
import com.gurpster.location.domain.model.LocationDomainModel
import io.reactivex.rxjava3.core.Flowable

interface LocationRepository {

    fun getLocation(): Flowable<LocationDomainModel>

    fun getAddress(): Flowable<AddressDomainModel>

}