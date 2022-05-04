package com.gurpster.octopus.extensions

fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }