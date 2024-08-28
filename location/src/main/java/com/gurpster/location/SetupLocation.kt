package com.gurpster.location

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gurpster.location.di.locationDataSourceModule
import com.gurpster.location.di.locationModules
import com.gurpster.location.di.locationRepositoryModule
import com.gurpster.location.di.locationUseCaseModule
import com.gurpster.location.di.locationViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun Fragment.setupLocation(context: Context, config: Config = Config()) {
    startKoin {
        androidLogger()
        androidContext(context)
        modules(
            module { factory { config } },
            locationDataSourceModule,
            locationRepositoryModule,
            locationUseCaseModule,
            locationViewModelModule
        )
    }
}

fun AppCompatActivity.setupLocation() {
    startKoin {
        androidLogger()
        androidContext(applicationContext)
        modules(locationModules)
    }
}