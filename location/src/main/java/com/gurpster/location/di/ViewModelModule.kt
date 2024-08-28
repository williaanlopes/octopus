package com.gurpster.location.di

import com.gurpster.location.view.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val locationViewModelModule = module {
    viewModelOf(::LocationViewModel)
}