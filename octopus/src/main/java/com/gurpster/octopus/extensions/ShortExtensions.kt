package com.gurpster.octopus.extensions

fun Short?.safe(default: Short = 0): Short = this ?: default

fun ShortArray?.safe(default: ShortArray = shortArrayOf()): ShortArray = this ?: default