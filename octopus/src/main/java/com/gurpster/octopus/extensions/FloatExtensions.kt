package com.gurpster.octopus.extensions

import android.content.res.Resources

fun Float?.toBoolean() = this != null && this >= 1

fun Float?.isTrue() = this != null && this >= 1

fun Float?.isFalse() = this == null || this <= 0

val Float.MIN_VALUE: Float
    get() = 0F

val Float.MIN_POSITIVE: Float
    get() = 1.0F

val Float.MIN_NEGATIVE: Float
    get() = -1.0F

val Float.dp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)
val Float.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)


fun Float.toKilometers() = this * 1.60934

fun Float.toMiles() = this / 1.60934

/**
 * @return `true` if the receiver is negative.
 */
fun Float.isNegative() = this < 0f

/**
 * @return `true` if the receiver is positive.
 */
fun Float.isPositive() = this > 0f

/**
 * @return `true` it the receiver is zero.
 */
fun Float.isZero() = this == 0f

/**
 * @return `true` if the receiver is not negative.
 */
fun Float.isNotNegative() = this >= 0f

/**
 * @return `true` if the receiver is not positive.
 */
fun Float.isNotPositive() = this <= 0f

/**
 * @return `true` it the receiver is not zero.
 */
fun Float.isNotZero() = this != 0f

/**
 * @return `true` if the receiver is `null` or negative.
 */
fun Float?.isNullOrNegative() = this == null || this < 0f

/**
 * @return `true` if the receiver is `null` or positive.
 */
fun Float?.isNullOrPositive() = this == null || this > 0f

/**
 * @return `true` it the receiver is `null` or zero.
 */
fun Float?.isNullOrZero() = this == null || this == 0f

val Float.Companion.SIZE_BYTES: Int
    get() = 4

fun Float?.safe(default: Float = 0f): Float = this ?: default

fun FloatArray?.safe(default: FloatArray = floatArrayOf()): FloatArray = this ?: default