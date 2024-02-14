package com.gurpster.octopus.extensions

import android.os.BaseBundle
import android.os.Build
import android.os.Bundle
import androidx.annotation.CheckResult
import androidx.annotation.RequiresApi

/**
 * @param T The type of the value.
 * @param key The key or `null`.
 * @return The value associated with the given key.
 * Returns `null` if no value is associated with the given key,
 * a `null` value is associated with the given key or the value is not from type [T].
 */
@CheckResult
inline fun <reified T> Bundle.getOrNull(key: String?): T? {
    return this.get(key) as? T?
}

/**
 * @param T The type of the value.
 * @param key The key or `null`.
 * @param defaultValue The default value is returned when called on a `null` reference,
 * no value is associated with the given key, a `null` value is associated with the given key
 * or the value is not from type [T].
 * @return The value associated with the given key otherwise the [defaultValue].
 */
@CheckResult
inline fun <reified T> Bundle?.get(key: String?, defaultValue: T): T {
    return (this?.get(key) as? T?) ?: defaultValue
}

/**
 * @param T The type of the value.
 * @param key The key or `null`.
 * @return The value associated with the given key.
 * Returns `null` if no value is associated with the given key,
 * a `null` value is associated with the given key or the value is not from type [T].
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@CheckResult
inline fun <reified T> BaseBundle.getOrNull(key: String?): T? {
    return this.get(key) as? T?
}

/**
 * @param T The type of the value.
 * @param key The key or `null`.
 * @param defaultValue The default value is returned when called on a `null` reference,
 * no value is associated with the given key, a `null` value is associated with the given key
 * or the value is not from type [T].
 * @return The value associated with the given key otherwise the [defaultValue].
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@CheckResult
inline fun <reified T> BaseBundle?.get(key: String?, defaultValue: T): T {
    return (this?.get(key) as? T?) ?: defaultValue
}

fun Bundle.asMap() = keySet().associateWith { get(it) }