package com.gurpster.octopus.extensions

import android.app.Service
import android.os.Build

/**
 * Compatibility method for [Service.stopForeground]. On versions below [Build.VERSION_CODES.N]
 * this methods calls the old [Service.stopForeground] method and passes true as parameter
 * when the [notificationBehavior] is [Service.STOP_FOREGROUND_REMOVE]. Otherwise false is passed.
 * On newer android versions this just calls the new [Service.stopForeground] method.
 * @param notificationBehavior One of the below listed constants.
 * @see Service.STOP_FOREGROUND_REMOVE
 * @see Service.STOP_FOREGROUND_DETACH
 * @see Service.STOP_FOREGROUND_LEGACY
 */
fun Service.stopForegroundCompat(
    notificationBehavior: Int
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        stopForeground(notificationBehavior)
    } else {
        @Suppress("DEPRECATION", "InlinedApi")
        stopForeground(notificationBehavior == Service.STOP_FOREGROUND_REMOVE)
    }
}

/**
 * Compatibility method for [Service.stopForeground]. On versions below [Build.VERSION_CODES.N]
 * this methods calls the old [Service.stopForeground] method and passes true as parameter
 * to remove the notification.
 * On newer android versions this just calls the new [Service.stopForeground] method
 * with [Service.STOP_FOREGROUND_REMOVE] as parameter.
 */
fun Service.stopForegroundAndRemoveNotification() {
    // noinspection InlinedApi
    stopForegroundCompat(notificationBehavior = Service.STOP_FOREGROUND_REMOVE)
}

/**
 * Compatibility method for [Service.stopForeground]. On versions below [Build.VERSION_CODES.N]
 * this methods calls the old [Service.stopForeground] method and passes false as parameter
 * to not remove the notification.
 * On newer android versions this just calls the new [Service.stopForeground] method
 * with [Service.STOP_FOREGROUND_DETACH] as parameter.
 */
fun Service.stopForegroundAndDetachNotification() {
    // noinspection InlinedApi
    stopForegroundCompat(notificationBehavior = Service.STOP_FOREGROUND_DETACH)
}