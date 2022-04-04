package com.gurpster.sample.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gurpster.sample.reflections.findClass
import com.gurpster.sample.reflections.getBinding

inline fun <V : ViewBinding> BottomSheetDialogFragment.viewBindings(
    inflater: LayoutInflater,
    container: ViewGroup?
): V {
    return findClass().getBinding(inflater, container)
}