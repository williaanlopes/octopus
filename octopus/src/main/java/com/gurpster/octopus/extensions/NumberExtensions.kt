package com.gurpster.octopus.extensions

import android.content.res.Resources

fun Number?.forceLong(): Long {
    return this?.toLong() ?: 0
}

fun Number?.forceDouble(): Double {
    return this?.toDouble() ?: 0.0
}

fun Number?.forceFloat(): Float {
    return this?.toFloat() ?: 0.0F
}

fun Number?.forceInt(): Int {
    return this?.toInt() ?: 0
}