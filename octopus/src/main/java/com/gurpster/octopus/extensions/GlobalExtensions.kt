package com.gurpster.octopus.extensions

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
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

fun getRandomFilepath(
    context: Context,
    extension: String,
    directory: String = Environment.DIRECTORY_PICTURES
): String {
    return "${context.getExternalFilesDir(directory)?.absolutePath}/${System.currentTimeMillis()}.$extension"
}

fun getUriFromPath(context: Context, fileProvider: String, path: String): Uri {
    return FileProvider.getUriForFile(
        context,
        fileProvider,//"${BuildConfig.APPLICATION_ID}.fileprovider",
        File(path)
    )
}

fun getRandomUri(
    context: Context,
    fileProvider: String,
    extension: String = "jpeg",
    directory: String = Environment.DIRECTORY_PICTURES
): Uri {
    return getUriFromPath(context, fileProvider, getRandomFilepath(context, extension, directory))
}

fun scaleDown(realImage: Bitmap, maxImageSize: Float, filter: Boolean): Bitmap? {
    val ratio = Math.min(maxImageSize / realImage.width, maxImageSize / realImage.height)
    val width = Math.round(ratio * realImage.width)
    val height = Math.round(ratio * realImage.height)
    return Bitmap.createScaledBitmap(realImage, width, height, filter)
}

fun Any.emptyString() = String()

