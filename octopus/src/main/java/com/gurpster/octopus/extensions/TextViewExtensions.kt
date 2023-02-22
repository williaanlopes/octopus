package com.gurpster.octopus.extensions

import android.widget.TextView
import com.gurpster.octopus.helpers.TextViewHelper

fun TextView.asyncText(text: CharSequence, textSize: Int = text.length) {
    async(this, text, textSize)
}

fun TextView.asyncText(text: CharSequence) {
    async(this, text, text.length)
}

fun TextView.getString() = text.toString()

private fun async(view: TextView, text: CharSequence, textSize: Int = text.length) {
    TextViewHelper.asyncText(view, text, textSize)
}

fun TextView.toText() = text.toString().formalizeText()