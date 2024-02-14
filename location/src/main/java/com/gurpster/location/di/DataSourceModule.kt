package com.gurpster.location.di

import com.gurpster.location.Config
import com.gurpster.location.data.source.LocationDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule = module {
//    factoryOf(::Config)
    singleOf(::LocationDataSource)
}