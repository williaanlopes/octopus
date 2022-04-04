package com.gurpster.sample.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar

inline fun View.toggleVisibility() {
    when (this.visibility) {
        View.VISIBLE -> this.visibility = View.GONE
        View.INVISIBLE,
        View.GONE -> this.visibility = View.VISIBLE
    }
}

inline fun View.show(boolean: Boolean) {
    if (boolean) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

inline fun View.showIfHaveText(text: String) {
    if (!text.isNullOrBlank()) this.visibility = View.VISIBLE
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