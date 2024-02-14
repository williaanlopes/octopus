package com.gurpster.octopus.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.gurpster.octopus.helpers.FragmentAutoClearedValue
import com.gurpster.octopus.helpers.autoCleared

fun <T : ViewBinding> Fragment.viewBinding(
    binder: (View) -> T,
) = FragmentAutoClearedValue(binder)

fun <T : ViewBinding> Fragment.viewBindings() = autoCleared<T>()

// val firstName by bundleArgs<String>("firstName") // String?
@Suppress("DEPRECATION")
inline fun <reified T : Any> Fragment.bundleArgs(key: String, defaultValue: T? = null) = lazy {
    val value = arguments?.get(key)
    if (value is T) value else defaultValue
}

fun Fragment.navigate(directions: NavDirections) {
    findNavController().navigate(directions)
}

fun Fragment.navigate(@IdRes redId: Int) {
    findNavController().navigate(redId)
}

fun Fragment.navigate(@IdRes resId: Int, args: Bundle? = null, navOptions: NavOptions? = null) =
    findNavController().navigate(resId, args, navOptions)

inline fun <F : Fragment, reified V : Any> F.navDirections() = autoCleared<V>()
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

fun Fragment.isAppInstalled(packageName: String): Boolean =
    requireContext().isAppInstalled(packageName)

fun Fragment.installApp(packageName: String) {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
        )
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

fun Fragment.openApp(packageName: String, shouldInstall: Boolean = false) {
    val launchIntent = requireContext().packageManager
        .getLaunchIntentForPackage("com.package.address")

    if (launchIntent != null) {
        startActivity(launchIntent) //null pointer check in case package name was not found
    } else if (shouldInstall) {
        installApp(packageName)
    }
}

inline fun Fragment.supplyContext(block: Activity.() -> Unit) {
    activity?.run { block(this) }
}


fun Fragment.finish() {
    supplyContext { finish() }
}

/**
 *  Fragment related
 *
 * val firstName by getValue<String>("firstName") // String?
 * val lastName by getValueNonNull<String>("lastName") // String
 */
inline fun <reified T : Any> Fragment.getValue(lable: String, defaultvalue: T? = null) = lazy {
    val value = arguments?.get(lable)
    if (value is T) value else defaultvalue
}

inline fun <reified T : Any> Fragment.getValueNonNull(lable: String, defaultvalue: T? = null) =
    lazy {
        val value = arguments?.get(lable)
        requireNotNull(if (value is T) value else defaultvalue) { lable }
    }