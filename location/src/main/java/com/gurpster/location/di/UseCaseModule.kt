package com.gurpster.location.di

import com.gurpster.location.domain.usecase.GetLocationUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::GetLocationUseCase)
}