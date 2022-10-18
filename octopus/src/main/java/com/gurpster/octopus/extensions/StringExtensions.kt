package com.gurpster.octopus.extensions

import android.location.Location
import android.net.Uri
import android.text.Spanned
import android.util.Base64.NO_WRAP
import android.util.Base64.encode
import android.util.Patterns
import android.webkit.URLUtil
import androidx.core.text.HtmlCompat
import java.io.*
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and
import kotlin.text.Charsets.UTF_8

@Throws(IOException::class)
fun String.serialize(): String {
    if (this.isEmpty()) return ""
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

fun String.md5(): String = MessageDigest.getInstance("MD5")
    .digest(toByteArray(UTF_8))
    .toHex()

fun String.sha256(): String = MessageDigest.getInstance("SHA-256")
    .digest(this.toByteArray(UTF_8))
    .toHex()

fun String.toUUID(): String {
    return UUID.nameUUIDFromBytes(encodeToByteArray()).toString()
}

/**
 * Is valid email
 *
 * @return boolean valid email
 */
fun String.isValidEmail(): Boolean =
    isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidUrl(): Boolean =
    isNotEmpty() && Patterns.WEB_URL.matcher(this).matches()

fun String.singleWhitespace(): String = replace("""\s+""".toRegex(), " ")

fun String.singleMultiLine(): String = replace("""\n+""".toRegex(), "\n")

fun String.formalizeText(): String {
    return trim()
        .singleWhitespace()
        .singleMultiLine()
}

/**
 * Capitalize first letter in all string
 * <b>source: capitalize</b>
 * <b>: Capitalize</b>
 * @return String capitalized word
 */
fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") {
        it.lowercase().replaceFirstChar(Char::titlecase)
    }

fun String.capitalizeFirst(): String = replaceFirstChar(Char::titlecase)

fun String.toHtml(): Spanned {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY);
}

fun String.toBase64Encode(flag: Int = NO_WRAP): String {
    return String(encode(this.encodeToByteArray(), flag))
}

fun String.Companion.randomUUID(): String = UUID.randomUUID().toString()

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

val String.Companion.DATE_FORMAT_DATABASE: String
    get() = "yyyy-MM-dd HH:mm:ss"

/**
 * Remove white spaces
 *
 * @return String
 */
fun String.removeWhitespaces() = replace(" ", "")

fun String.toPriceAmount(): String {
    val dec = DecimalFormat("###,###,###.00")
    return dec.format(this.toDouble())
}


/**
 * Returns an date as string with format
 *
 * <pre>
 * {@code
 * val format = "yyyy-MM-dd HH:mm:ss"
 * val date = Date()
 * val str = date.toString(format)
 * val date2 = str.toDate(format)
 * }
 * </pre>
 *
 * @param  format the string date format
 * @return Date as String
 */
@Throws(ParseException::class)
fun String.toDate(format: String): Date? {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    try {
        return dateFormatter.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    throw ParseException("Please Enter a valid date", 0)
}

/**
 * To location
 *
 * <pre>
 * {@code
 * val apiLoc = "41.6168, 41.6367".toLocation("API")
 * }
 * </pre>
 *
 * @param provider
 * @return
 */
fun String.toLocation(provider: String): Location? {
    val components = this.split(",")
    if (components.size != 2) {
        return null
    }

    val lat = components[0].toDoubleOrNull() ?: return null
    val lng = components[1].toDoubleOrNull() ?: return null
    val location = Location(provider);

    location.latitude = lat
    location.longitude = lng

    return location
}

/**
 * Contains digit
 * <pre>
 * {@code
 * val digitsOnly = "12345".containsDigitOnly
 * val notDigitsOnly = "abc12345".containsDigitOnly
 * }
 * </pre>
 */
val String.containsDigit: Boolean
    get() = matches(Regex(".*[0-9].*"))

/**
 * Is alphanumeric
 *
 * <pre>
 * {@code
 * val alphaNumeric = "abc123".isAlphanumeric
 * val notAlphanumeric = "ab.2a#1".isAlphanumeric
 * }
 * </pre>
 */
val String.isAlphanumeric: Boolean
    get() = matches(Regex("[a-zA-Z0-9]*"))

/**
 * As uri
 *
 * <pre>
 * {@code
 * val uri = "invalid_uri".asUri
 * val uri2 = "https://medium.com/@alex_nekrasov".asUri
 * }
 * </pre>
 */
val String.asUri: Uri?
    get() = try {
        if (URLUtil.isValidUrl(this)) {
            Uri.parse(this)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }

/**
 * Check if a String is valid or not
 * </br>
 * Validation is != null && != "null" && != ""
 *
 * @return true/false for valid String
 */
fun String?.valid(): Boolean =
    this != null && !this.equals("null", true) && this.trim().isNotEmpty()

val String?.onlyNumbers: Int
    get() = if (this.isNullOrBlank()) {
        0
    } else {
        this.filter { it.isDigit() }.toInt()
    }

val String?.onlyStringNumbers: String
    get() = if (this.isNullOrBlank()) {
        ""
    } else {
        this.filter { it.isDigit() }
    }

@Throws(ParseException::class)
fun String.getDateUnixTime(): Long {
    try {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.parse(this)!!.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    throw ParseException("Please Enter a valid date", 0)
}

fun String.encodeBytes(bytes: ByteArray): String {
    val strBuf = StringBuffer()
    for (i in bytes.indices) {
        strBuf.append(((bytes[i].toInt() shr 4 and 0xF) + 'a'.code).toChar())
        strBuf.append(((bytes[i] and 0xF) + 'a'.code).toChar())
    }
    return strBuf.toString()
}

fun String.decodeBytes(str: String): ByteArray {
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