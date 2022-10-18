package com.gurpster.octopus.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long?.toBoolean() = this != null && this == 1L

fun Long?.isTrue() = this != null && this >= 1L

fun Long?.isFalse() = this == null || this <= 0

val Long.MIN_VALUE: Long
    get() = 0L

val Long?.MIN_POSITIVE: Long
    get() = 1L

val Long.MIN_NEGATIVE: Long
    get() = -1L

fun Long.getTimeStamp(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Long.getYearMonthDay(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Long.getHour(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}