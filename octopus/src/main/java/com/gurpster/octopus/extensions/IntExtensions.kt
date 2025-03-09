package com.gurpster.octopus.extensions

import android.content.res.Resources
import java.text.DecimalFormat

fun Int?.orZero() = this ?: 0

fun Int?.toBoolean() = this != null && this >= 1

fun Int?.isTrue() = this != null && this >= 1

fun Int?.isFalse() = this == null || this <= 0

val Int.MIN_VALUE: Int
    get() = 0

val Int.MIN_POSITIVE: Int
    get() = 1

val Int.MIN_NEGATIVE: Int
    get() = -1

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.formatPrice(format: String): String {
    val formatter = DecimalFormat(format)
    return formatter.format(this)
}

/**
 * @param flag The flag to check.
 * @return True if the receiver has all flag bits in [flag] set.
 */
fun Int.isFlagSet(flag: Int): Boolean {
    return this.and(flag) == flag
}

/**
 * @param flag The bit flag to add to the receiver.
 * @return The receiver with all flag bits from [flag] added.
 */
fun Int.addFlag(flag: Int): Int {
    return this.or(flag)
}

/**
 * @param flag The bit flag to remove from the receiver.
 * @return The receiver with all flag bits from [flag] removed.
 */
fun Int.removeFlag(flag: Int): Int {
    return this.and(flag.inv())
}

/**
 * @return An array with the set bit flags.
 */
fun Int.getBitFlags(): Array<Int> {
    val found = ArrayList<Int>(countOneBits())
    for (i in 0..31) {
        val nextFlag = 1.shl(i)
        if (this.and(nextFlag) == nextFlag) {
            found.add(nextFlag)
        }
    }
    return found.toTypedArray()
}

/**
 * @return `true` if the receiver is negative.
 */
fun Int.isNegative() = this < 0

/**
 * @return `true` if the receiver is positive.
 */
fun Int.isPositive() = this > 0

/**
 * @return `true` it the receiver is zero.
 */
fun Int.isZero() = this == 0

/**
 * @return `true` if the receiver is not negative.
 */
fun Int.isNotNegative() = this >= 0

/**
 * @return `true` if the receiver is not positive.
 */
fun Int.isNotPositive() = this <= 0

/**
 * @return `true` it the receiver is not zero.
 */
fun Int.isNotZero() = this != 0

/**
 * @return `true` if the receiver is `null` or negative.
 */
fun Int?.isNullOrNegative() = this == null || this < 0

/**
 * @return `true` if the receiver is `null` or positive.
 */
fun Int?.isNullOrPositive() = this == null || this > 0

/**
 * @return `true` it the receiver is `null` or zero.
 */
fun Int?.isNullOrZero() = this == null || this == 0

/**
 * is it even or odd
 */
fun Int.isEven() = this.mod(2) == 0
fun Int.isOdd() = this.mod(2) != 0

/**
 * Reverses the digits of an integer.
 *
 * Example:
 * ```
 * println(1234.reverse()) // 4321
 * ```
 * @return The integer with its digits reversed.
 */
fun Int.reverse(): Int {
    return this.toString().reversed().toInt()
}

/**
 * Checks whether an integer is a perfect square.
 *
 * Example:
 * ```
 * println(16.isSquare()) //   true
 * println(17.isSquare()) //   false
 * ```
 * @return True if the integer is a perfect square, false otherwise.
 */
fun Int.isSquare(): Boolean {
    val sqrt = kotlin.math.sqrt(this.toDouble()).toInt()
    return sqrt * sqrt == this
}

/**
 * This function formats an integer by adding commas as thousands separators, making it
 * easier to read and understand large numbers.The formatted output is returned as a string.
 *
 * Example:
 * ```
 * println(1234567.formatWithCommas()) // 1,234,567
 * ```
 * @return A string representation of the integer value with commas added as thousands separators.
 */
fun Int.formatWithCommas(): String {
    return String.format("%,d", this)
}

/**
 * This function formats an integer by adding commas as thousands separators, making it
 * easier to read and understand large numbers.The formatted output is returned as a string.
 *
 * Example:
 * ```
 * println(1234567.formatWithDots()) // 1.234.567
 * ```
 * @return A string representation of the integer value with commas added as thousands separators.
 */
fun Int.formatWithDots(): String {
    return String.format("%.d", this)
}

fun Int?.safe(default: Int = 0): Int = this ?: default

fun IntArray?.safe(default: IntArray = intArrayOf()): IntArray = this ?: default

fun IntRange?.safe(default: IntRange = 0..0): IntRange = this ?: default