package com.gurpster.octopus.extensions

fun Int?.toBoolean() = this != null && this >= 1

fun Int?.isTrue() = this != null && this >= 1

fun Int?.isFalse() = this == null || this <= 0

val Int.MIN_VALUE: Int
    get() = 0

val Int.MIN_POSITIVE: Int
    get() = 1

val Int.MIN_NEGATIVE: Int
    get() = -1