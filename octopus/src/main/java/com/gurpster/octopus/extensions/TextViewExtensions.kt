package com.gurpster.sample.extensions

import android.widget.TextView
import com.gurpster.sample.helpers.TextViewHelper

fun TextView.asyncText(text: CharSequence, textSize: Int?) {
    TextViewHelper.asyncText(this, text, textSize)
}

fun TextView.asyncText(text: CharSequence) {
    TextViewHelper.asyncText(this, text, null)
}