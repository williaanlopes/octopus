package com.gurpster.octopus.extensions

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.CheckResult
import androidx.annotation.Px
import kotlin.math.roundToInt

val Int.DP
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

val Float.DP
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.SP
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        toFloat(),
        Resources.getSystem().displayMetrics
    )

val Float.SP
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )

/**
 * The current value interpreted as density-independent pixels and converted to pixels.
 */
@get:CheckResult
@get:Px
inline val Int.dpInPx get() = this.times(Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * The current value interpreted as pixels and converted to density-independent pixels.
 */
@get:CheckResult
inline val Int.pxInDp get() = this.div(Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * The current value interpreted as scale-independent pixels and converted to pixels.
 */
@get:CheckResult
@get:Px
inline val Int.spInPx
    get() = this.times(Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

/**
 * The current value interpreted as pixels and converted to scale-independent pixels.
 */
@get:CheckResult
inline val Int.pxInSp
    get() = this.div(Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

/**
 * @return Convert the current value from density-independent pixels to pixels.
 */
@CheckResult
@Px
fun Int.dpToPx() = this.times(Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * @return Convert the current value from pixels to density-independent pixels.
 */
@CheckResult
fun Int.pxToDp() = this.div(Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * @return Convert the current value from scale-independent pixels to pixels.
 */
@CheckResult
@Px
fun Int.spToPx() =
    this.times(Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

/**
 * @return Convert the current value from pixels to scale-independent pixels.
 */
@CheckResult
fun Int.pxToSp() = this.div(Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

/**
 * @param unit The unit to convert the value from. The possible values are listed below.
 * @return The current value treated as [unit] converted to pixels.
 * @see TypedValue.COMPLEX_UNIT_PX
 * @see TypedValue.COMPLEX_UNIT_DIP
 * @see TypedValue.COMPLEX_UNIT_SP
 * @see TypedValue.COMPLEX_UNIT_PT
 * @see TypedValue.COMPLEX_UNIT_IN
 * @see TypedValue.COMPLEX_UNIT_MM
 */
@CheckResult
@Px
fun Int.toPx(unit: Int): Int {
    return TypedValue.applyDimension(unit, toFloat(), Resources.getSystem().displayMetrics)
        .roundToInt()
}

/**
 * The current value interpreted as density-independent pixels and converted to pixels.
 */
@get:CheckResult
@get:Px
inline val Float.dpInPx
    get() = this.times(Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * The current value interpreted as pixels and converted to density-independent pixels.
 */
@get:CheckResult
inline val Float.pxInDp get() = this.div(Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * The current value interpreted as scale-independent pixels and converted to pixels.
 */
@get:CheckResult
@get:Px
inline val Float.spInPx
    get() = this.times(Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

/**
 * The current value interpreted as pixels and converted to scale-independent pixels.
 */
@get:CheckResult
inline val Float.pxInSp
    get() = this.div(Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

/**
 * @return Convert the current value from density-independent pixels to pixels.
 */
@CheckResult
@Px
fun Float.dpToPx() = this.times(Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * @return Convert the current value from pixels to density-independent pixels.
 */
@CheckResult
fun Float.pxToDp() = this.div(Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * @return Convert the current value from scale-independent pixels to pixels.
 */
@CheckResult
@Px
fun Float.spToPx() = this.times(Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

/**
 * @return Convert the current value from pixels to scale-independent pixels.
 */
@CheckResult
fun Float.pxToSp() = this.div(Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

/**
 * @param unit The unit to convert the value from. The possible values are listed below.
 * @return The current value treated as [unit] converted to pixels.
 * @see TypedValue.COMPLEX_UNIT_PX
 * @see TypedValue.COMPLEX_UNIT_DIP
 * @see TypedValue.COMPLEX_UNIT_SP
 * @see TypedValue.COMPLEX_UNIT_PT
 * @see TypedValue.COMPLEX_UNIT_IN
 * @see TypedValue.COMPLEX_UNIT_MM
 */
@CheckResult
@Px
fun Float.toPx(unit: Int): Int {
    return TypedValue.applyDimension(unit, this, Resources.getSystem().displayMetrics).roundToInt()
}