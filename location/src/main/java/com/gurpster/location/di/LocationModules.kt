package com.gurpster.location.di

import org.koin.dsl.module

val locationModules = module {
    locationDataSourceModule
    locationRepositoryModule
    locationUseCaseModule
    locationViewModelModule
}