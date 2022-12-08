package com.gurpster.octopus.extensions

import com.gurpster.octopus.DATE_FORMAT_DATABASE
import java.text.SimpleDateFormat
import java.util.*

/**
 * Returns a String as an Date with format
 *
 * <pre>
 * {@code
 * val format = "yyyy-MM-dd HH:mm:ss"
 * val locale = Locale.getDefault()
 * }
 * </pre>
 *
 * @param  format the string date format
 * @param  locale the language
 * @return Date as String
 */
fun Date.getFormat(
    format: String = "yyyy-MM-dd HH:mm:ss",
    locale: Locale = Locale.getDefault(),
): String {
    val dateFormat = SimpleDateFormat(format, locale)
    return dateFormat.format(Date())
}

/**
 * Returns timestamp format date to persist in database
 *
 * @param
 * @return Date in format "yyyy-MM-dd HH:mm:ss"
 */
val Date.timeStamp: String
    get() = getFormat(DATE_FORMAT_DATABASE)

/**
 * Returns a String as an Date with format
 *
 * <pre>
 * {@code
 * val format = "yyyy-MM-dd HH:mm:ss"
 * val locale = Locale.getDefault()
 * }
 * </pre>
 *
 * @param  format the string date format
 * @param  locale language
 * @return Date as String
 */
fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val dateFormatter = SimpleDateFormat(format, locale)
    return dateFormatter.format(this)
}

fun Date.getCurrentMonth(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MONTH)
}

fun Date.getCurrentYear(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.YEAR)
}

fun Date.getCurrentDay(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Date.getCurrentWeek(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_WEEK)
}

/**
 * Returns a String month name
 *
 * <pre>
 * {@code
 * val date = Date()
 * val style = Calendar.LONG | Calendar.SHORT
 * val locale = Locale.getDefault(),
 * }
 * </pre>
 *
 * @param  date
 * @param  style long or short name
 * @param  locale language
 * @return Month Name String
 */
fun Date.monthName(
    style: Int = Calendar.LONG,
    locale: Locale = Locale.getDefault(),
): String? = getMonthName(this, style, locale)

/**
 * Returns a String day name
 *
 * <pre>
 * {@code
 * val date = Date()
 * val style = Calendar.LONG | Calendar.SHORT
 * val locale = Locale.getDefault(),
 * }
 * </pre>
 *
 * @param  date
 * @param  style long or short name
 * @param  locale language
 * @return Day Name String
 */
fun Date.dayName(
    style: Int = Calendar.LONG,
    locale: Locale = Locale.getDefault(),
): String? = getDayName(this, style, locale)

/**
 * Returns a String month name
 *
 * <pre>
 * {@code
 * val date = Date()
 * val style = Calendar.LONG | Calendar.SHORT
 * val locale = Locale.getDefault(),
 * }
 * </pre>
 *
 * @param  date
 * @param  style long or short name
 * @param  locale language
 * @return Month Name String
 */
fun getMonthName(
    date: Date,
    style: Int = Calendar.LONG,
    locale: Locale = Locale.getDefault(),
): String? {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.getDisplayName(
        Calendar.MONTH,
        style,
        locale
    )
}

/**
 * Returns a String day name
 *
 * <pre>
 * {@code
 * val date = Date()
 * val style = Calendar.LONG | Calendar.SHORT
 * val locale = Locale.getDefault(),
 * }
 * </pre>
 *
 * @param  date
 * @param  style long or short name
 * @param  locale language
 * @return Day Name String
 */
fun getDayName(
    date: Date,
    style: Int = Calendar.LONG,
    locale: Locale = Locale.getDefault(),
): String? {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.getDisplayName(
        Calendar.DAY_OF_WEEK,
        style,
        locale
    )
}
