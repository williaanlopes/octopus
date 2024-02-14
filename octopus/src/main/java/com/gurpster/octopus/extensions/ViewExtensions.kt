package com.gurpster.octopus.extensions

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CheckResult
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.widget.TooltipCompat
import com.google.android.material.snackbar.Snackbar
import com.gurpster.octopus.helpers.inputMethodManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun View.toggleVisibility() {
    when (this.visibility) {
        View.VISIBLE -> this.visibility = View.GONE
        View.INVISIBLE,
        View.GONE -> this.visibility = View.VISIBLE
    }
}

fun View.show(boolean: Boolean) {
    if (boolean) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun View.showIfHaveText(text: String) {
    if (text.isNotBlank()) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.remove(){
    this.visibility = View.GONE
}

// Snackbar Extensions
fun View.showShotSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.showLongSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

@SuppressLint("ShowToast")
fun View.snackBarWithAction(
    message: String, actionlable: String,
    block: () -> Unit
) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(actionlable) {
            block()
        }
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) { }
    return false
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.invisible(boolean: Boolean) {
    if (boolean) this.visibility = View.INVISIBLE
    else this.visibility = View.VISIBLE
}

/**
 * Set the view's visibility to [View.VISIBLE] if the [condition] is `true`.
 * @param condition `true` to make the view visible. `false` to make the view [otherwise].
 * @param otherwise The visibility applied if [condition] is `false`. If `null` is passed the
 * visibility is not touched if [condition] is `false`.
 */
fun View.setVisibleIf(condition: Boolean, otherwise: Int? = View.GONE) {
    visibility = if (condition) View.VISIBLE else otherwise ?: return
}

/**
 * Set the top and bottom padding to [value].
 * @param T The type of the view.
 * @param value The padding value in pixel.
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : View> T.setHorizontalPadding(@Px value: Int): T {
    setPadding(value, paddingTop, value, paddingBottom)
    return this
}

/**
 * Set the left and right padding to [value].
 * @param T The type of the view.
 * @param value The padding value in pixel.
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : View> T.setVerticalPadding(@Px value: Int): T {
    setPadding(paddingLeft, value, paddingRight, value)
    return this
}

/**
 * `true` if this view has focus itself, or is the ancestor of the view that has focus.
 * Setting this to `true` does no guarantee that the view gets focus. See [View.requestFocus] for details.
 * @see View.hasFocus
 * @see View.requestFocus
 * @see View.clearFocus
 */
@get:CheckResult
inline var View.hasFocusCompat: Boolean
    get() = hasFocus()
    set(value) {
        if (value) requestFocus() else clearFocus()
    }

/**
 * `true` if the current view is the currently active view for the input method.
 */
@get:CheckResult
inline val View.isInputActive get() = context.inputMethodManager.isActive(this)

/**
 * @param visible `true` to show the soft input method for the current view. `false` to hide it if it
 * is currently active for the current view.
 */
fun View.setSoftInputVisibility(visible: Boolean = true) {
    if (visible) {
        context.inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    } else if (context.inputMethodManager.isActive(this)) {
        context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

/**
 * Calls [View.startDragAndDrop] starting from [Build.VERSION_CODES.N] and
 * [View.startDrag] on older versions.
 * @param data Optional [ClipData] with data to be transferred by the drag and drop operation.
 * @param shadowBuilder Used for building the drag shadow.
 * @param myLocalState Will later be available in [DragEvent.getLocalState].
 * @param flags See possible flags in [View.startDragAndDrop].
 * @return `true` if the method completes successfully, or `false` if it fails anywhere.
 */
fun View.startDragAndDropCompat(
    data: ClipData? = null,
    shadowBuilder: View.DragShadowBuilder = View.DragShadowBuilder(this),
    myLocalState: Any? = this,
    flags: Int = 0
): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        startDragAndDrop(data, shadowBuilder, myLocalState, flags)
    } else {
        @Suppress("DEPRECATION")
        startDrag(data, shadowBuilder, myLocalState, flags)
    }
}

/**
 * Same as [View.setContentDescription] but accepts a string resource.
 * @param resId The content description for the view. `null` resets the content description.
 */
fun View.setContentDescription(@StringRes resId: Int?) {
    contentDescription = if (resId == null) null else context.getString(resId)
}

/**
 * Same as [View.setStateDescription] but accepts a string resource.
 * @param resId Describes the state of the view and is used for accessibility support.
 * `null` restores the default behaviour.
 */
@RequiresApi(Build.VERSION_CODES.R)
fun View.setStateDescription(@StringRes resId: Int?) {
    stateDescription = if (resId == null) null else context.getString(resId)
}

@CheckResult
fun View.clicks(): Flow<View> = callbackFlow {
    checkMainThread()

    setOnClickListener { trySend(it) }
    awaitClose { setOnClickListener(null) }
}

/**
 * Same as [View.setAccessibilityPaneTitle] but accepts a string resource.
 * @param resId The title of the pane. `null` indicates that the receiver is not a pane.
 */
@RequiresApi(Build.VERSION_CODES.P)
fun View.setAccessibilityPaneTitle(@StringRes resId: Int?) {
    accessibilityPaneTitle = if (resId == null) null else context.getString(resId)
}

/**
 * Compatibility method to to emulate the behavior of [View.setTooltipText] prior to API level 26
 * using [TooltipCompat]. On higher API levels this method just calls [View.setTooltipText].
 * @param resId The tooltip text. `null` if no tooltip is required.
 */
fun View.setTooltipTextCompat(@StringRes resId: Int?) {
    TooltipCompat.setTooltipText(this, if (resId == null) null else context.getString(resId))
}

/**
 * Compatibility method to to emulate the behavior of [View.setTooltipText] prior to API level 26
 * using [TooltipCompat]. On higher API levels this method just calls [View.setTooltipText].
 * @param tooltipText The tooltip text. `null` if no tooltip is required.
 */
fun View.setTooltipTextCompat(tooltipText: CharSequence?) {
    TooltipCompat.setTooltipText(this, tooltipText)
}

fun View.setMargins(
    leftMarginDp: Int? = null,
    topMarginDp: Int? = null,
    rightMarginDp: Int? = null,
    bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = this.dp }
        topMarginDp?.run { params.topMargin = this.dp }
        rightMarginDp?.run { params.rightMargin = this.dp }
        bottomMarginDp?.run { params.bottomMargin = this.dp }
        requestLayout()
    }
}

fun View.clearMargins() {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = 0.dp
        params.topMargin = 0.dp
        params.rightMargin = 0.dp
        params.bottomMargin = 0.dp
        requestLayout()
    }
}

fun View.takeScreenshot(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val bgDrawable = this.background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }
    this.draw(canvas)
    return bitmap
}

/**
 * Start The FadeIn Animation on This View
 */
fun View.fadeIn(duration: Int = 400) {
    clearAnimation()
    val alphaAnimation = AlphaAnimation(this.alpha, 1.0f)
    alphaAnimation.duration = duration.toLong()
    startAnimation(alphaAnimation)
}

/**
 * Start the FadeOut Animation on This View
 */
fun View.fadeOut(duration: Int = 400) {
    clearAnimation()
    val alphaAnimation = AlphaAnimation(this.alpha, 0.0f)
    alphaAnimation.duration = duration.toLong()
    startAnimation(alphaAnimation)
}

/**
 * will show the view If Condition is true else make if INVISIBLE or GONE Based on the [makeInvisible] flag
 */
fun View.showIf(boolean: Boolean, makeInvisible: Boolean = false) {
    visibility = if (boolean) View.VISIBLE else if (makeInvisible) View.INVISIBLE else View.GONE
}

/**
 * will hide the view If Condition is true else make if INVISIBLE or GONE Based on the [makeInvisible] flag
 */
fun View.hideIf(boolean: Boolean, makeInvisible: Boolean = false) {
    showIf(boolean.not(), makeInvisible)
}

/**
 * will enable the view If Condition is true else enables It
 */

fun View.enableIf(boolean: Boolean) {
    isEnabled = boolean
}

/**
 * will disable the view If Condition is true else enables It
 */

fun View.disableIf(boolean: Boolean) = run { isEnabled = boolean.not() }

/**
 * set View Padding From Left
 */
fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)

/**
 * set View Padding From Right
 */
fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

/**
 * set View Padding From Top
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun View.setPaddingTop(value: Int) =
    setPaddingRelative(paddingStart, value, paddingEnd, paddingBottom)

/**
 * set View Padding From Bottom
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun View.setPaddingBottom(value: Int) =
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, value)

/**
 * set View Padding From Start
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun View.setPaddingStart(value: Int) =
    setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)

/**
 * set View Padding From End
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun View.setPaddingEnd(value: Int) =
    setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)

/**
 * set View Padding On Horizontal Edges
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun View.setPaddingHorizontal(value: Int) =
    setPaddingRelative(value, paddingTop, value, paddingBottom)

/**
 * set View Padding From Vertical Edges
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun View.setPaddingVertical(value: Int) = setPaddingRelative(paddingStart, value, paddingEnd, value)

/**
 * set height
 */
fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

/**
 * set Width
 */
fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}

/**
 * resize the View Width Height
 */
fun View.resize(width: Int, height: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = width
        lp.height = height
        layoutParams = lp
    }
}



