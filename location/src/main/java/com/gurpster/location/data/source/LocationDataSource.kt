package com.gurpster.location.data.source

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.gurpster.location.Config
import com.gurpster.location.data.entity.AddressEntity
import com.gurpster.location.data.entity.LocationEntity
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.PublishSubject

private const val LOCATION_REQUEST_INTERVAL = 10000L
private const val LOCATION_REQUEST_FASTEST_INTERVAL = 5000L

class LocationDataSource(context: Context, configuration: Config) {

    private val locationSubject = PublishSubject.create<LocationEntity>()
    private val addressSubject = PublishSubject.create<AddressEntity>()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest: LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        LOCATION_REQUEST_INTERVAL
    ).apply {
        setWaitForAccurateLocation(configuration.waitForAccurateLocation)
        setMinUpdateIntervalMillis(configuration.minUpdateIntervalMillis)
        setMaxUpdateDelayMillis(configuration.maxUpdateDelayMillis)
        setMaxUpdates(configuration.stopAfterUpdates)
    }.build()

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach(::setLocation)
        }
    }

    val locationObservable: Flowable<LocationEntity> =
        locationSubject.toFlowable(BackpressureStrategy.MISSING)
            .doOnSubscribe { startLocationUpdates() }
            .doOnCancel { stopLocationUpdates() }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.lastLocation.addOnSuccessListener(::setLocation)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun setLocation(location: Location) {
        locationSubject.onNext(
            LocationEntity(
                location.latitude,
                location.longitude
            )
        )
    }

    val addressObservable: Flowable<AddressEntity> =
        addressSubject.toFlowable(BackpressureStrategy.MISSING)
            .doOnSubscribe { reverseGeocode() }

    private fun reverseGeocode() {
//        locationSubject.map { it.toDomain() }
    }
}