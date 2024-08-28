package com.gurpster.location.di

import com.gurpster.location.data.repository.LocationRepositoryImpl
import com.gurpster.location.domain.repository.LocationRepository
import org.koin.dsl.module

val locationRepositoryModule = module {
    single<LocationRepository> { LocationRepositoryImpl(get()) }
}