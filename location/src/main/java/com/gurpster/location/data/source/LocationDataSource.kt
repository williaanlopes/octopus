package com.gurpster.location.data.source

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
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

class LocationDataSource(val context: Context, configuration: Config) {

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

    @SuppressLint("CheckResult")
    private fun reverseGeocode() {
//        locationSubject.toMap { location ->
//            Geocoder(context, Locale.getDefault())
//                .getAddress(location.latitude, location.longitude) { address: Address? ->
//                    if (address != null) {
//                        locationSubject.onNext(
//                            LocationEntity(
//                                location.latitude,
//                                location.longitude
//                            )
//                        )
//                    }
//                }
//        }
    }
}

@Suppress("DEPRECATION")
fun Geocoder.getAddress(
    latitude: Double,
    longitude: Double,
    address: (android.location.Address?) -> Unit
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getFromLocation(latitude, longitude, 1) { address(it.firstOrNull()) }
        return
    }

    try {
        address(getFromLocation(latitude, longitude, 1)?.firstOrNull())
    } catch (e: Exception) {
        address(null)
    }
}