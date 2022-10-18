package com.gurpster.octopus.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.getFormat(format: String): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(Date())
}

val Date.getDataBaseTimeStamp: String
    get() = getFormat(DATE_FORMAT_DATABASE)

/**
 * Returns a String as an Date with format
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
fun Date.toString(format: String): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.format(this)
}
