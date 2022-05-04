package com.gurpster.octopus.extensions

import android.util.Patterns
import java.io.*
import java.security.MessageDigest
import kotlin.experimental.and
import kotlin.text.Charsets.UTF_8

@Throws(IOException::class)
fun String.serialize(): String {
    val serialObj = ByteArrayOutputStream()
    val objStream = ObjectOutputStream(serialObj)
    objStream.writeObject(this)
    objStream.close()
    return encodeBytes(serialObj.toByteArray())
}

@Throws(IOException::class, ClassNotFoundException::class)
fun String.deserialize(): String {
    if (this.isEmpty()) return ""
    val serialObj = ByteArrayInputStream(decodeBytes(this))
    val objStream = ObjectInputStream(serialObj)
    return objStream.readObject().toString()
}

fun String.md5(): String = MessageDigest.getInstance("MD5").digest(this.toByteArray(UTF_8)).toHex()

fun String.sha256(str: String): String =
    MessageDigest.getInstance("SHA-256").digest(str.toByteArray(UTF_8)).toHex()

fun String.isValidEmail(): Boolean =
    this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidUrl(): Boolean =
    this.isNotEmpty() && Patterns.WEB_URL.matcher(this).matches()

//fun String.isValidPhoneNumber(region: String): Boolean {
//    val phoneNumberKit = PhoneNumberUtil.getInstance()
//    val number = phoneNumberKit.parse(this, region)
//    return phoneNumberKit.isValidNumber(number)
//}
//
//fun String.formatPhoneNumber(region: String): String? {
//    val phoneNumberKit = PhoneNumberUtil.getInstance()
//    val number = phoneNumberKit.parse(this, region)
//    if (!phoneNumberKit.isValidNumber(number))
//        return null
//
//    return phoneNumberKit.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
//}


// *****************************************************
private fun encodeBytes(bytes: ByteArray): String {
    val strBuf = StringBuffer()
    for (i in bytes.indices) {
        strBuf.append(((bytes[i].toInt() shr 4 and 0xF) + 'a'.code).toChar())
        strBuf.append(((bytes[i] and 0xF) + 'a'.code).toChar())
    }
    return strBuf.toString()
}

private fun decodeBytes(str: String): ByteArray {
    val bytes = ByteArray(str.length / 2)
    var i = 0
    while (i < str.length) {
        var c = str[i]
        bytes[i / 2] = (c - 'a' shl 4).toByte()
        c = str[i + 1]
        bytes[i / 2] = bytes[i / 2].plus(((c - 'a'))).toByte()
        i += 2
    }
    return bytes
}