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