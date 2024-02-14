package com.gurpster.octopus.extensions

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.CheckResult
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.gurpster.octopus.helpers.TextViewHelper

fun TextView.asyncText(text: CharSequence, textSize: Int = text.length) {
    async(this, text, textSize)
}

fun TextView.asyncText(text: CharSequence) {
    async(this, text, text.length)
}

private fun async(view: TextView, text: CharSequence, textSize: Int = text.length) {
    TextViewHelper.asyncText(view, text, textSize)
}

fun TextView.getString() = text.toString()

fun TextView.toText() = text.toString().formalizeText()

/**
 * The style of the current [Typeface].
 * @see Typeface.NORMAL
 * @see Typeface.BOLD
 * @see Typeface.ITALIC
 * @see Typeface.BOLD_ITALIC
 */
@get:CheckResult
inline var TextView.textStyle: Int
    get() = typeface.style
    set(value) = setTypeface(typeface, value)

/**
 * Same as [TextView.getText] but returning [String] instead instead of [CharSequence].
 */
@get:CheckResult
inline var TextView.textString: String
    get() = text.toString()
    set(value) {
        text = value
    }

/**
 * Clear the text in the TextView.
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : TextView> T.clear(): T {
    text = ""
    return this
}

/**
 * Clear the error message in the TextView.
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : TextView> T.clearError(): T {
    setError(null, null)
    return this
}

/**
 * Same as [TextView.setError] but accepts a string resource.
 * @param resId The string resource id of the error message.
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : TextView> T.setError(@StringRes resId: Int): T {
    error = context.getString(resId)
    return this
}

/**
 * Same as [TextView.setError] but accepts a string resource.
 * @param resId The string resource id of the error message.
 * @param icon The right-hand compound drawable of the TextView.
 * The drawable must already have had [Drawable.setBounds] set on it.
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : TextView> T.setError(@StringRes resId: Int, icon: Drawable?): T {
    setError(context.getString(resId), icon)
    return this
}

/**
 * Same as [TextView.setError] but accepts a string resource.
 * @param resId The string resource id of the error message.
 * @param iconResId The resource for the right-hand compound drawable of the TextView.
 * [Drawable.setBounds] will be called with the [Drawable.getIntrinsicWidth] and [Drawable.getIntrinsicHeight].
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : TextView> T.setError(@StringRes resId: Int, @DrawableRes iconResId: Int): T {
    setError(context.getString(resId), context.getDrawableCompatOrNull(iconResId)?.apply {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    })
    return this
}

/**
 * Same as [TextView.setError] but accepts a string resource.
 * @param error The error message.
 * @param iconResId The resource for the right-hand compound drawable of the TextView.
 * [Drawable.setBounds] will be called with the [Drawable.getIntrinsicWidth] and [Drawable.getIntrinsicHeight].
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : TextView> T.setError(error: CharSequence, @DrawableRes iconResId: Int): T {
    setError(error, context.getDrawableCompatOrNull(iconResId)?.apply {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    })
    return this
}

fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.deleteLine() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.bold(isBold: Boolean = true) {
    paint.isFakeBoldText = isBold
    paint.isAntiAlias = true
}