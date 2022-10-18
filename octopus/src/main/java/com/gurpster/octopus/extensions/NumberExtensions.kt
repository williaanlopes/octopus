package com.gurpster.octopus.extensions

fun Number?.forceLong(): Long {
    return this?.toLong() ?: 0
}

fun Number?.forceDouble(): Double {
    return this?.toDouble() ?: 0.0
}

fun Number?.forceInt(): Int {
    return this?.toInt() ?: 0
}