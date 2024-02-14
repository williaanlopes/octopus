package com.gurpster.location.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gurpster.location.domain.usecase.GetLocationUseCase
import com.gurpster.location.extensions.baseSubscribe
import com.gurpster.location.extensions.compositeDisposable
import com.gurpster.location.view.model.LocationModel

@Suppress("MemberVisibilityCanBePrivate")
class LocationViewModel(
    private val stateHandle: SavedStateHandle,
    private val getLocationUseCase: GetLocationUseCase,
) : ViewModel() {

    private val _locationModel = MutableLiveData<LocationModel?>()
    val locationModel: LiveData<LocationModel?> = _locationModel

    fun onLocationPermissionGranted() {
        getLocationUseCase
            .build()
            .map { it.toModel() }
            .baseSubscribe(
                onSuccess = ::onGetLocationSuccess,
                onError = ::onGetLocationError
            )
    }

    fun onLocationPermissionDenied() {

    }

    private fun onGetLocationSuccess(locationModel: LocationModel) {
        _locationModel.postValue(locationModel)
    }

    private fun onGetLocationError(throwable: Throwable) {
        _locationModel.postValue(null)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}