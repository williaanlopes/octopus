package com.gurpster.octopus.extensions

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.gurpster.octopus.helpers.AutoClearedValue
import com.gurpster.octopus.helpers.FragmentAutoClearedValue

fun <T : ViewBinding> Fragment.viewBinding(
    binder: (View) -> T
) = FragmentAutoClearedValue(binder)

fun <T : ViewBinding> Fragment.viewBindings() = AutoClearedValue<T>(this)

// val firstName by bundleArgs<String>("firstName") // String?
inline fun <reified T : Any> Fragment.bundleArgs(lable: String, defaultvalue: T? = null) = lazy {
    val value = arguments?.get(lable)
    if (value is T) value else defaultvalue
}

fun Fragment.navigate(@NonNull directions: NavDirections) {
    findNavController().navigate(directions)
}

fun Fragment.navigate(@IdRes redId: Int) {
    findNavController().navigate(redId)
}

fun Fragment.navigate(@IdRes resId: Int, args: Bundle? = null, navOptions: NavOptions? = null) =
    findNavController().navigate(resId, args, navOptions)

inline fun <F : Fragment, reified V : Any> F.navDirections() = AutoClearedValue<V>(this)
//inline fun <F : Fragment, reified V : Any> F.navDirections() = inject<V> {
//    parametersOf(this)
//}

fun Fragment.getNavigatorGraphId() = findNavController().currentDestination?.parent?.id ?: 0

fun Fragment.getResourceName(id: Int): String = resources.getResourceEntryName(id)

fun Fragment.popBackStack() = findNavController().popBackStack()

fun Fragment.getParentFragmentType(): Fragment =
    requireParentFragment().childFragmentManager.fragments[0]

fun Fragment.shortToast(text: String) =
    Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()

fun Fragment.longToast(text: String) =
    Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()

fun Fragment.toast(text: String, length: Int) =
    Toast.makeText(requireContext(), text, length).show()
