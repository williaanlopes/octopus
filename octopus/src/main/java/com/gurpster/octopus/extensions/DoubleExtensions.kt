package com.gurpster.octopus.extensions

import java.text.DecimalFormat

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