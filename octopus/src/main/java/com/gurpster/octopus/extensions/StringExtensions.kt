package com.gurpster.octopus.extensions

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Base64.DEFAULT
import android.util.Base64.NO_WRAP
import android.util.Base64.decode
import android.util.Base64.encode
import android.util.Patterns
import android.webkit.URLUtil
import androidx.annotation.CheckResult
import androidx.core.text.HtmlCompat
import com.gurpster.octopus.DATE_FORMAT
import java.io.*
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.Normalizer
import java.text.Normalizer.normalize
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import kotlin.text.Charsets.UTF_8

val String.EMPTY: String
    get() = ""

val String.SPACE: String
    get() = " "

val String.UNDERSCORE: String
    get() = "_"

val String.BACK_SLASH: String
    get() = "\\"

val String.SLASH: String
    get() = "/"

val String.PIPE: String
    get() = "|"

val String.Companion.DATE_FORMAT_DATABASE: String
    get() = "yyyy-MM-dd HH:mm:ss"

val String.Companion.YYYY_MM_DD_FORMAT: String
    get() = "yyyy-MM-dd"

val String.Companion.YYYY_MM_FORMAT: String
    get() = "yyyy-MM"

val String.Companion.MM_DD_FORMAT: String
    get() = "MM-dd"

val String.Companion.YYYY_FORMAT: String
    get() = "yyyy"

val String.Companion.HH_MM_SS_FORMAT: String
    get() = "HH:mm:ss"

val String.Companion.HH_MM_FORMAT: String
    get() = "HH:mm"

val String.Companion.HH_FORMAT: String
    get() = "HH"

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
fun String.toDate(format: String = "yyyy/MM/dd hh:mm"): Date? {
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

/**
 * Check if the receiver is a valid filename on Android and Linux systems using ext2/ext3/ext4 or F2FS.
 * More precisely, this method checks if the receiver is not empty,
 * does not contain a reserved character ('/' or 'NUL') and is not larger than 255 bytes (UTF-8).
 * @return True if the name is valid, false otherwise.
 */
@CheckResult
fun String.isValidFileName(): Boolean {
    return this != "" && !contains('/') && !contains('\u0000') && encodeToByteArray().size <= 255
}

/**
 * Interprets the receiver as HTML string and returns displayable styled text.
 * @param imageGetter Any <img> tags in the HTML will use this [Html.ImageGetter]
 * to request a representation of the image.
 * @param tagHandler To handle unknown tags.
 * @param legacy `true` to use [Html.FROM_HTML_MODE_LEGACY] instead of [Html.FROM_HTML_MODE_COMPACT]
 * starting from [Build.VERSION_CODES.N]. On older versions [Html.FROM_HTML_MODE_LEGACY] is always used.
 * @return The displayable styled text.
 */
@CheckResult
fun String.toHtml(
    imageGetter: Html.ImageGetter? = null,
    tagHandler: Html.TagHandler? = null,
    legacy: Boolean = false,
): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(
            this,
            if (legacy) Html.FROM_HTML_MODE_LEGACY else Html.FROM_HTML_MODE_COMPACT,
            imageGetter,
            tagHandler
        )
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this, imageGetter, tagHandler)
    }
}

/**
 * @return A new [StringBuilder] with the current CharSequence.
 */
fun CharSequence.toStringBuilder() = StringBuilder(this)

/**
 * @return A newly created string with [delimiter] added as prefix.
 */
fun String.addPrefix(delimiter: Char) = delimiter + this

/**
 * @return A newly created string with [delimiter] added as prefix.
 */
fun String.addPrefix(delimiter: String) = delimiter + this

/**
 * @return A newly created string with [delimiter] added as suffix.
 */
fun String.addSuffix(delimiter: Char) = this + delimiter

/**
 * @return A newly created string with [delimiter] added as suffix.
 */
fun String.addSuffix(delimiter: String) = this + delimiter

/**
 * @return A newly created string with [delimiter] added as suffix and prefix.
 */
fun String.addSurrounding(delimiter: Char) = delimiter + this + delimiter

/**
 * @return A newly created string with [delimiter] added as suffix and prefix.
 */
fun String.addSurrounding(delimiter: String) = delimiter + this + delimiter

/**
 * Same as [CharSequence.indexOf] but returns `null` if the [char] was not found.
 * @param char The character to look for.
 * @param startIndex The index (including) to start searching from.
 * @param ignoreCase `true` to ignore character case when matching a character. By default `false`.
 * @return The index of the first occurrence of [char] or `null` if none is found.
 */
fun CharSequence.indexOfOrNull(
    char: Char,
    startIndex: Int = 0,
    ignoreCase: Boolean = false
): Int? {
    val result = indexOf(char = char, startIndex = startIndex, ignoreCase = ignoreCase)
    return if (result == -1) null else result
}

/**
 * Same as [CharSequence.indexOf] but returns `null` if the [string] was not found.
 * @param string The [String] to look for.
 * @param startIndex The index (including) to start searching from.
 * @param ignoreCase `true` to ignore character case when matching a character. By default `false`.
 * @return The index of the first occurrence of [string] or `null` if none is found.
 */
fun CharSequence.indexOfOrNull(
    string: String,
    startIndex: Int = 0,
    ignoreCase: Boolean = false
): Int? {
    val result = indexOf(string = string, startIndex = startIndex, ignoreCase = ignoreCase)
    return if (result == -1) null else result
}

/**
 * Same as [CharSequence.lastIndexOf] but returns `null` if the [char] was not found.
 * @param char The character to look for.
 * @param startIndex The index of character to start searching at.
 * The search proceeds backward toward the beginning of the string.
 * @param ignoreCase `true` to ignore character case when matching a character. By default `false`.
 * @return The index of the last occurrence of [char] or `null` if none is found.
 */
fun CharSequence.lastIndexOfOrNull(
    char: Char,
    startIndex: Int = lastIndex,
    ignoreCase: Boolean = false
): Int? {
    val result = lastIndexOf(char = char, startIndex = startIndex, ignoreCase = ignoreCase)
    return if (result == -1) null else result
}

/**
 * Same as [CharSequence.lastIndexOf] but returns `null` if the [string] was not found.
 * @param string The [String] to look for.
 * @param startIndex The index of character to start searching at.
 * The search proceeds backward toward the beginning of the string.
 * @param ignoreCase `true` to ignore character case when matching a character. By default `false`.
 * @return The index of the last occurrence of [string] or `null` if none is found.
 */
fun CharSequence.lastIndexOfOrNull(
    string: String,
    startIndex: Int = lastIndex,
    ignoreCase: Boolean = false
): Int? {
    val result = lastIndexOf(string = string, startIndex = startIndex, ignoreCase = ignoreCase)
    return if (result == -1) null else result
}

/**
 * @return `true` if this char sequence contains no characters or only whitespace characters.
 */
fun CharSequence.isEmptyOrBlank(): Boolean {
    return isEmpty() || isBlank()
}

/**
 * @return `true` if this char sequence contains characters other than whitespace characters.
 */
fun CharSequence.isNotEmptyOrBlank(): Boolean {
    return !isEmptyOrBlank()
}

fun String.Companion.empty() = ""

/**
 * url validation
 */
fun String.isUrl(): Boolean {
    return URLUtil.isValidUrl(this)
}

/**
 * phone number validation
 */
fun String.isPhoneNumber(): Boolean {
    return android.util.Patterns.PHONE.matcher(this).matches()
}


/**
 * hex to RGB converter
 */
fun String.hexToRGB(): Triple<String, String, String> {
    var name = this
    if (!name.startsWith("#")) {
        name = "#$this"
    }
    val color = Color.parseColor(name)
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)

    return Triple(red.toString(), green.toString(), blue.toString())
}

fun <T : CharSequence> T?.safe(default: T = "" as T): T = this ?: default

fun CharSequence?.isNotNullOrEmpty() = !isNullOrEmpty()

fun CharSequence?.isNotNullOrBlank() = !isNullOrBlank()

/**
 * Returns a new File Object with the Current String as Its path
 */
fun String.toFile() = File(this)

/**
 * Encode String to URL
 */
fun String.encodeToUrl(charSet: String = "UTF-8"): String = URLEncoder.encode(this, charSet)

/**
 * Decode String to URL
 */
fun String.decodeToUrl(charSet: String = "UTF-8"): String = URLDecoder.decode(this, charSet)

/**
 * Converts the String to Title Case
 */
fun String.toTitleCase(): String {
    return substring(0, 1).uppercase() + substring(1).lowercase()
}

/**
 * Converts the String to Camel Case
 */
fun String.toCamelCase(): String {
    if (length == 0)
        return this
    val parts = trim().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    var camelCaseString = ""
    for (part in parts) camelCaseString = camelCaseString + part.toTitleCase() + " "
    return camelCaseString
}

/**
 * Split String into Multiple SubStrings Based on the Value of [maxLength]
 */
fun String.splitSubStrings(maxLength: Int): Array<String> {
    val ret = ArrayList<String>()
    var start = 0
    while (start < length) {
        ret.add(substring(start, Math.min(length, start + maxLength)))
        start += maxLength
    }
    return ret.toTypedArray()
}

/**
 * Encrypt String to AES with the specific Key
 */
@SuppressLint("GetInstance")
fun String.encryptAES(key: String): String {
    var crypted: ByteArray? = null
    try {
        val skey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, skey)
        crypted = cipher.doFinal(toByteArray())
    } catch (e: Exception) {
        println(e.toString())
    }
    return String(encode(crypted, DEFAULT))
}

/**
 * Decrypt String to AES with the specific Key
 */
@SuppressLint("GetInstance")
fun String.decryptAES(key: String): String {
    var output: ByteArray? = null
    try {
        val skey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, skey)
        output = cipher.doFinal(decode(this, DEFAULT))
    } catch (e: Exception) {
        println(e.toString())
    }
    return output?.let { String(it) } ?: ""
}

/**
 * encode The String to Binary
 */
fun String.encodeToBinary(): String {
    val stringBuilder = StringBuilder()
    toCharArray().forEach {
        stringBuilder.append(Integer.toBinaryString(it.code))
        stringBuilder.append(" ")
    }
    return stringBuilder.toString()
}

/**
 * Decode the String from binary
 */
fun String.deCodeToBinary(): String {
    val stringBuilder = StringBuilder()
    split(" ").forEach {
        stringBuilder.append(Integer.parseInt(it.replace(" ", ""), 2))
    }
    return stringBuilder.toString()
}

/**
 * Save String to a Given File
 */
fun String.saveToFile(file: File) = FileOutputStream(file).bufferedWriter().use {
    it.write(this)
    it.flush()
    it.close()
}

fun String.safeBoolean(default: Boolean = false) = try {
    toBoolean()
} catch (e: Exception) {
    default
}

fun String.safeByte(default: Byte = 0) = toByteOrNull().safe(default)

fun String.safeShort(default: Short = 0) = toShortOrNull().safe(default)

fun String.safeInt(default: Int = 0) = toIntOrNull().safe(default)

fun String.safeLong(default: Long = 0L) = toLongOrNull().safe(default)

fun String.safeFloat(default: Float = 0f) = toFloatOrNull().safe(default)

/**
 *
 * @return String some text; Other text as Enum format SOME_TEXT; OTHER_TEXT.
 */
fun String.safeDouble(default: Double = 0.0) = toDoubleOrNull().safe(default)

/**
 * Convert String to enum formatter
 * @return String formated as ENUM.
 */
fun String.toEnum() = Regex("\\p{InCOMBINING_DIACRITICAL_MARKS}+")
    .replace(normalize(trim(), Normalizer.Form.NFD), EMPTY)
    .replace(SPACE, UNDERSCORE)
    .uppercase()

// =========== only internal fun =================

internal fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}

// Private Method Below....
private fun encrypt(string: String?, type: String): String {
    val bytes = MessageDigest.getInstance(type).digest(string!!.toByteArray())
    return bytes2Hex(bytes)
}