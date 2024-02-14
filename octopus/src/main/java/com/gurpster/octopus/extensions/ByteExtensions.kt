package com.gurpster.octopus.extensions

fun Byte?.safe(default: Byte = 0): Byte = this ?: default