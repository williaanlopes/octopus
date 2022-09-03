package com.gurpster.octopus.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gurpster.octopus.reflections.findClass
import com.gurpster.octopus.reflections.getBinding
import java.io.File

private fun findBinary(binaryName: String = "su"): Boolean {
    var found = false
    if (!found) {
        val places = arrayOf(
            "/sbin/", "/system/bin/", "/system/xbin/",
            "/data/local/xbin/", "/data/local/bin/",
            "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"
        )
        for (where in places) {
            if (File(where + binaryName).exists()) {
                found = true
                break
            }
        }
    }
    return found
}

fun isRooted(): Boolean {
    return findBinary()
}

fun isDevMode(context: Context): Boolean {
    return when {
        Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN -> {
            Settings.Secure.getInt(context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0
        }
        Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN -> {
            @Suppress("DEPRECATION")
            Settings.Secure.getInt(context.contentResolver,
                Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0
        }
        else -> false
    }
}

