package com.gurpster.sample.ui.notifications

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.gurpster.location.Config
import com.gurpster.location.setupLocation
import com.gurpster.location.view.LocationViewModel
import com.gurpster.octopus.BindingFragment
import com.gurpster.sample.databinding.FragmentNotificationsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationsFragment : BindingFragment<FragmentNotificationsBinding>() {

    private val viewModel by viewModel<LocationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val config = Config(
            stopAfterUpdates = 2
        )
        setupLocation(requireContext(), config)
        startLocationPermissionRequest()
        viewModel.locationModel.observe(viewLifecycleOwner) {
            Log.d("NotificationsFragment", "$it")
        }
        return super.onCreateView(
            inflater,
            container,
            savedInstanceState
        )
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onLocationPermissionGranted()
        } else {
            viewModel.onLocationPermissionDenied()
        }
    }

    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}