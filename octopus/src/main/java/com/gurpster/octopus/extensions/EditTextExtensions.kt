package com.gurpster.octopus.extensions

import android.text.TextUtils
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText

fun EditText.getString() = text.toString()

fun TextInputEditText.string(): String {
    return when {
        TextUtils.isEmpty(this.text) -> ""
        else -> return this.text.toString()
    }
}

fun TextInputEditText.getOnlyNumbers(): Int {
    return this.text.toString().trim().onlyNumbers
}

fun EditText.getText(): String {
    return when {
        TextUtils.isEmpty(this.text) -> ""
        else -> return this.text.toString()
    }
}

fun EditText.getTextNumbers(): Int {
    return this.text.toString().trim().onlyNumbers
}