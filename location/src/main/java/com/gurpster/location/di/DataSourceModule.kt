package com.gurpster.location.di

import com.gurpster.location.data.source.LocationDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val locationDataSourceModule = module {
    singleOf(::LocationDataSource)
}