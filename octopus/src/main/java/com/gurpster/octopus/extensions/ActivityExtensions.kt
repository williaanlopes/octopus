package com.gurpster.octopus.extensions

import android.content.ActivityNotFoundException
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

@Suppress("DEPRECATION")
inline fun <reified T : Any> ComponentActivity.bundleArgs(lable: String, defaultvalue: T? = null) =
    lazy {
        val value = intent?.extras?.get(lable)
        if (value is T) value else defaultvalue
    }

fun ComponentActivity.shortToast(text: String) =
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun ComponentActivity.longToast(text: String) =
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun ComponentActivity.toast(text: String, length: Int) =
    Toast.makeText(this, text, length).show()

fun ComponentActivity.isAppInstalled(packageName: String): Boolean = isAppInstalled(packageName)

fun ComponentActivity.installApp(packageName: String) {
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

fun ComponentActivity.openApp(packageName: String, shouldInstall: Boolean = false) {
    val launchIntent = packageManager.getLaunchIntentForPackage("com.package.address")
    if (launchIntent != null) {
        startActivity(launchIntent) //null pointer check in case package name was not found
    } else if (shouldInstall) {
        installApp(packageName)
    }
}

fun ComponentActivity.hideKeyboard() {
    if (currentFocus != null) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

fun AppCompatActivity.findNavController(@IdRes navHost: Int): NavController {
    val navHostFragment = supportFragmentManager.findFragmentById(navHost) as NavHostFragment?
    return navHostFragment!!.navController
}
