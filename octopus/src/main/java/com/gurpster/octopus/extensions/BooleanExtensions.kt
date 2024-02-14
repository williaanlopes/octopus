package com.gurpster.octopus.extensions

fun Boolean?.forceBoolean() = this == true

/**
 * Function that is supposed to mimic Java's beloved ternary operator.
 * Unlike the ternary operator in java, this function does not perform short-circuit evaluation.
 * Both [ifTrue] and [ifFalse] will always be evaluated.
 * @param T The type of the two parameters and the return value.
 * @param ifTrue Returned if the boolean is `true`.
 * @param ifFalse Returned if the boolean is `false`.
 * @return [ifTrue] or [ifFalse] based on the value of the boolean.
 */
inline fun <reified T> Boolean.ternary(ifTrue: T, ifFalse: T): T {
    return if (this) ifTrue else ifFalse
}

/**
 * Converts Boolean to Int, if true then 1 else 0
 */
fun Boolean.toInt(): Int = if (this) 1 else 0

/**
 * Toggle the Boolean Value, if it's true then it will become false else vice versa.
 */
fun Boolean.toggle() = !this

fun Boolean?.safe(default: Boolean = false): Boolean = this ?: default

fun BooleanArray?.safe(default: BooleanArray = booleanArrayOf()): BooleanArray = this ?: default