package com.gurpster.octopus.extensions

import java.text.DecimalFormat

fun Double?.orZero() = this ?: 0

fun Double?.toBoolean() = this != null && this == 1.0

fun Double?.isTrue() = this != null && this >= 1.0

fun Double?.isFalse() = this == null || this <= 0

val Double.MIN_VALUE: Double
    get() = 0.0

val Double.MIN_POSITIVE: Double
    get() = 1.0

val Double.MIN_NEGATIVE: Double
    get() = -1.0

fun Double.toPriceAmount(): String {
    val dec = DecimalFormat("###,###,###.00")
    return dec.format(this)
}

fun Double.formatPrice(format: String): String {
    val formatter = DecimalFormat(format)
    return formatter.format(this)
}

fun Double.toKilometers() = this * 1.60934

fun Double.toMiles() = this / 1.60934

/**
 * @return `true` if the receiver is negative.
 */
fun Double.isNegative() = this < 0.0

/**
 * @return `true` if the receiver is positive.
 */
fun Double.isPositive() = this > 0.0

/**
 * @return `true` it the receiver is zero.
 */
fun Double.isZero() = this == 0.0

/**
 * @return `true` if the receiver is not negative.
 */
fun Double.isNotNegative() = this >= 0.0

/**
 * @return `true` if the receiver is not positive.
 */
fun Double.isNotPositive() = this <= 0.0

/**
 * @return `true` it the receiver is not zero.
 */
fun Double.isNotZero() = this != 0.0

/**
 * @return `true` if the receiver is `null` or negative.
 */
fun Double?.isNullOrNegative() = this == null || this < 0.0

/**
 * @return `true` if the receiver is `null` or positive.
 */
fun Double?.isNullOrPositive() = this == null || this > 0.0

/**
 * @return `true` it the receiver is `null` or zero.
 */
fun Double?.isNullOrZero() = this == null || this == 0.0

//val Double.Companion.SIZE_BYTES: Int
//    get() = 8

fun Double?.safe(default: Double = 0.toDouble()): Double = this ?: default

fun DoubleArray?.safe(default: DoubleArray = doubleArrayOf()): DoubleArray = this ?: default