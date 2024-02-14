package com.gurpster.octopus.extensions

import com.gurpster.octopus.DATE_FORMAT
import com.gurpster.octopus.TIME_FORMAT
import com.gurpster.octopus.TIME_STAMP_FORMAT
import java.text.DecimalFormat
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

fun Long.formatPrice(format: String): String {
    val formatter = DecimalFormat(format)
    return formatter.format(this)
}

/**
 * @param flag The flag to check.
 * @return True if the receiver has all flag bits in [flag] set.
 */
fun Long.isFlagSet(flag: Long): Boolean {
    return this.and(flag) == flag
}

/**
 * @param flag The bit flag to add to the receiver.
 * @return The receiver with all flag bits from [flag] added.
 */
fun Long.addFlag(flag: Long): Long {
    return this.or(flag)
}

/**
 * @param flag The bit flag to remove from the receiver.
 * @return The receiver with all flag bits from [flag] removed.
 */
fun Long.removeFlag(flag: Long): Long {
    return this.and(flag.inv())
}

/**
 * @return An array with the set bit flags.
 */
fun Long.getBitFlags(): Array<Long> {
    val found = ArrayList<Long>(countOneBits())
    for (i in 0..31) {
        val nextFlag = 1L.shl(i)
        if (this.and(nextFlag) == nextFlag) {
            found.add(nextFlag)
        }
    }
    return found.toTypedArray()
}

/**
 * @return `true` if the receiver is negative.
 */
fun Long.isNegative() = this < 0L

/**
 * @return `true` if the receiver is positive.
 */
fun Long.isPositive() = this > 0L

/**
 * @return `true` it the receiver is zero.
 */
fun Long.isZero() = this == 0L

/**
 * @return `true` if the receiver is not negative.
 */
fun Long.isNotNegative() = this >= 0L

/**
 * @return `true` if the receiver is not positive.
 */
fun Long.isNotPositive() = this <= 0L

/**
 * @return `true` it the receiver is not zero.
 */
fun Long.isNotZero() = this != 0L

/**
 * @return `true` if the receiver is `null` or negative.
 */
fun Long?.isNullOrNegative() = this == null || this < 0L

/**
 * @return `true` if the receiver is `null` or positive.
 */
fun Long?.isNullOrPositive() = this == null || this > 0L

/**
 * @return `true` it the receiver is `null` or zero.
 */
fun Long?.isNullOrZero() = this == null || this == 0L

fun Long?.safe(default: Long = 0): Long = this ?: default

fun LongArray?.safe(default: LongArray = longArrayOf()): LongArray = this ?: default

fun LongRange?.safe(default: LongRange = 0L..0L): LongRange = this ?: default
