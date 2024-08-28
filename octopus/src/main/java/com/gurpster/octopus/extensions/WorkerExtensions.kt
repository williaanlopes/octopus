package com.gurpster.octopus.extensions

import android.widget.Toast
import androidx.work.Worker

fun Worker.shortToast(text: String) =
    Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()

fun Worker.longToast(text: String) =
    Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()

fun Worker.toast(text: String, length: Int) =
    Toast.makeText(applicationContext, text, length).show()