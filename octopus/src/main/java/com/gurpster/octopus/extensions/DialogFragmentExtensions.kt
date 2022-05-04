package com.gurpster.octopus.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gurpster.octopus.reflections.findClass
import com.gurpster.octopus.reflections.getBinding

fun <V : ViewBinding> BottomSheetDialogFragment.viewBindings(
    inflater: LayoutInflater,
    container: ViewGroup?
): V {
    return findClass().getBinding(inflater, container)
}