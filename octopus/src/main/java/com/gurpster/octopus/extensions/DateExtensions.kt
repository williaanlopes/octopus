package com.gurpster.octopus.extensions

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import com.gurpster.octopus.DATE_FORMAT_DATABASE
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.absoluteValue

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
 * Returns timestamp date to persist in database
 *
 * @param
 * @return Date in format "yyyy-MM-dd HH:mm:ss" string
 */
val Date.timeStamp: String
    get() = getFormat(DATE_FORMAT_DATABASE)

/**
 * Returns timestamp format date to persist in database
 *
 * @param
 * @return Date in format "yyyy-MM-dd HH:mm:ss"
 */
val Date.timeStampFormatter: String
    get() = "yyyy-MM-dd HH:mm:ss"

/**
 * Returns String UTC format date
 *
 * @param
 * @return Date in format "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
 */
val Date.utcFormatter: String
    get() = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

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

/**
 * Check if current date is after another compare using ChronoUnit
 * Can compara as MINUTES, SECONDS, MILLISECONDS, DAY, etc
 * For more information check <a href="">ChronoUnit</a> documentation
 *
 * @param comparable the value to compare
 * @param unit the unit to compare
 *
 * @return Boolean true or false
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.isAfter(comparable: Long, unit: ChronoUnit): Boolean {

    val dateFormat = SimpleDateFormat(
        this.utcFormatter,
        Locale.getDefault()
    )

    dateFormat.timeZone = TimeZone.getTimeZone(
        ZoneId.systemDefault()
    )

    val startDate = dateFormat.parse(
        dateFormat.format(this)
    )

    val endDate = dateFormat.parse(
        dateFormat.format(Date())
    )

    val diff = unit.between(
        startDate?.toInstant(),
        endDate?.toInstant()
    )

    return (diff > comparable)
}

/**
 * Check if current date is before another compare using ChronoUnit
 * Can compara as MINUTES, SECONDS, MILLISECONDS, DAY, etc
 * For more information check <a href="">ChronoUnit</a> documentation
 *
 * @param comparable the value to compare
 * @param unit the unit to compare
 *
 * @return Boolean true or false
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.isBefore(comparable: Long, unit: ChronoUnit): Boolean {

    val dateFormat = SimpleDateFormat(
        this.utcFormatter,
        Locale.getDefault()
    )

    dateFormat.timeZone = TimeZone.getTimeZone(
        ZoneId.systemDefault()
    )

    val startDate = dateFormat.parse(
        dateFormat.format(this)
    )

    val endDate = dateFormat.parse(
        dateFormat.format(Date())
    )

    val diff = unit.between(
        startDate?.toInstant(),
        endDate?.toInstant()
    )

    return (diff < comparable)

}

@SuppressLint("SimpleDateFormat")
fun Date.convertTo(format: String): String? {
    var dateStr: String? = null
    val df = SimpleDateFormat(format)
    try {
        dateStr = df.format(this)
    } catch (ex: Exception) {
        Timber.e("date", ex.toString())
    }

    return dateStr
}

// Converts current date to Calendar
fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

fun Date.isFuture(): Boolean {
    return !Date().before(this)
}

fun Date.isPast(): Boolean {
    return Date().before(this)
}

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this.time)
}

fun Date.isYesterday(): Boolean {
    return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.isTomorrow(): Boolean {
    return DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)
}

fun Date.today(): Date {
    return Date()
}

fun Date.yesterday(): Date {
    val cal = this.toCalendar()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    return cal.time
}

fun Date.tomorrow(): Date {
    val cal = this.toCalendar()
    cal.add(Calendar.DAY_OF_YEAR, 1)
    return cal.time
}

fun Date.hour(): Int {
    return this.toCalendar().get(Calendar.HOUR)
}

fun Date.minute(): Int {
    return this.toCalendar().get(Calendar.MINUTE)
}

fun Date.second(): Int {
    return this.toCalendar().get(Calendar.SECOND)
}

fun Date.month(): Int {
    return this.toCalendar().get(Calendar.MONTH) + 1
}

fun Date.monthName(locale: Locale = Locale.getDefault()): String? {
    return this.toCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, locale)
}

fun Date.year(): Int {
    return this.toCalendar().get(Calendar.YEAR)
}

fun Date.day(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_MONTH)
}

fun Date.dayOfWeek(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_WEEK)
}

fun Date.dayOfWeekName(locale: Locale = Locale.getDefault()): String? {
    return this.toCalendar().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale)
}

fun Date.dayOfYear(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_YEAR)
}

/**
 * This function calculates the number of years between two LocalDate objects.
 * It uses the ChronoUnit.YEARS enum value to calculate the difference in years between the two dates.
 * If the result is negative, it returns the absolute value of the difference.
 *
 * Example:
 * ```
 * val date1 = LocalDate.of(1998, 9, 24)
 * val date2 = LocalDate.of(2023, 3, 12)
 * println(date1.yearsDifference(date2)) // 24
 * ```
 * @param other: The other LocalDate object to calculate the difference from.
 * @return The number of years between the two LocalDate objects as a Long data type.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.yearsDifference(other: LocalDate): Long {
    return ChronoUnit.YEARS.between(this, other).absoluteValue
}

/**
 * This function calculates the number of months between two LocalDate objects.
 * It uses the ChronoUnit.MONTHS enum value to calculate the difference in months between the two dates.
 * If the result is negative, it returns the absolute value of the difference.
 *
 * Example:
 * ```
 * val date1 = LocalDate.of(1998, 9, 24)
 * val date2 = LocalDate.of(2023, 3, 12)
 * println(date1.monthsDifference(date2)) // 293
 * ```
 * @param other: The other LocalDate object to calculate the difference from.
 * @return The number of months between the two LocalDate objects as a Long data type.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.monthsDifference(other: LocalDate): Long {
    return ChronoUnit.MONTHS.between(this, other).absoluteValue
}

/**
 * This function calculates the number of days between two LocalDate objects.
 * It uses the ChronoUnit.DAYS enum value to calculate the difference in days between the two dates.
 * If the result is negative, it returns the absolute value of the difference.
 *
 * Example:
 * ```
 * val date1 = LocalDate.of(1998, 9, 24)
 * val date2 = LocalDate.of(2023, 3, 12)
 * println(date1.daysDifference(date2)) // 8935
 * ```
 * @param  other: The other LocalDate object to calculate the difference from.
 * @return The number of days between the two LocalDate objects as a Long data type.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.daysDifference(other: LocalDate): Long {
    return ChronoUnit.DAYS.between(this, other).absoluteValue
}
