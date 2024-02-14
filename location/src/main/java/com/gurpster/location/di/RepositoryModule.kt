package com.gurpster.location.di

import com.gurpster.location.data.repository.LocationRepositoryImpl
import com.gurpster.location.domain.repository.LocationRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LocationRepository> { LocationRepositoryImpl(get()) }
}