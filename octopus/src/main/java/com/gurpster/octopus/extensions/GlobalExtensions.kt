package com.gurpster.octopus.extensions

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.text.Editable
import androidx.annotation.CheckResult
import androidx.core.content.FileProvider
import com.gurpster.octopus.helpers.InMemoryCache
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import java.util.Date
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

private fun findBinary(binaryName: String = "su"): Boolean {
    var found = false
    val places = arrayOf(
        "/sbin/", "/system/bin/", "/system/xbin/",
        "/data/local/xbin/", "/data/local/bin/",
        "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"
    )
    for (where in places) {
        if (File(where + binaryName).exists()) {
            found = true
            break
        }
    }
    return found
}

fun isRooted(): Boolean {
    return findBinary()
}

fun getRandomFilepath(
    context: Context,
    extension: String,
    directory: String = Environment.DIRECTORY_PICTURES,
): String {
    return "${context.getExternalFilesDir(directory)?.absolutePath}/${System.currentTimeMillis()}.$extension"
}

fun getUriFromPath(context: Context, fileProvider: String, path: String): Uri {
    return FileProvider.getUriForFile(
        context,
        fileProvider,//"${BuildConfig.APPLICATION_ID}.fileprovider",
        File(path)
    )
}

fun getRandomUri(
    context: Context,
    fileProvider: String,
    extension: String = "jpeg",
    directory: String = Environment.DIRECTORY_PICTURES,
): Uri {
    return getUriFromPath(context, fileProvider, getRandomFilepath(context, extension, directory))
}

fun scaleDown(realImage: Bitmap, maxImageSize: Float, filter: Boolean): Bitmap? {
    val ratio = Math.min(maxImageSize / realImage.width, maxImageSize / realImage.height)
    val width = Math.round(ratio * realImage.width)
    val height = Math.round(ratio * realImage.height)
    return Bitmap.createScaledBitmap(realImage, width, height, filter)
}

fun Any.emptyString() = String()

/**
 * @return An [Editable] with the content of the current char-sequence.
 */
@CheckResult
fun CharSequence.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

/**
 * Invokes [block] with parameters [first] and [second] if both are non `null`.
 * @param F The type of the first [block] parameter.
 * @param S The type of the second [block] parameter.
 * @param R The type of the [block] result.
 * @param first First [block] parameter.
 * @param second Second [block] parameter.
 * @param block Invoked if [first] and [second] are non `null`.
 * @return `null` if either [first] or [second] is `null`. Otherwise the result of [block] is returned.
 */
@OptIn(ExperimentalContracts::class)
inline fun <F, S, R> biLetNonNull(first: F?, second: S?, block: (F, S) -> R): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (first == null || second == null) null else block(first, second)
}

/**
 * Invokes [block] with parameters [first], [second] and [third] if all are non `null`.
 * @param F The type of the first [block] parameter.
 * @param S The type of the second [block] parameter.
 * @param T The type of the third [block] parameter.
 * @param R The type of the [block] result.
 * @param first First [block] parameter.
 * @param second Second [block] parameter.
 * @param third Third [block] parameter.
 * @param block Invoked if [first], [second] and [third] are non `null`.
 * @return `null` if either [first], [second] or [third] is `null`. Otherwise the result of [block] is returned.
 */
@OptIn(ExperimentalContracts::class)
inline fun <F, S, T, R> triLetNonNull(first: F?, second: S?, third: T?, block: (F, S, T) -> R): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (first == null || second == null || third == null) null else block(
        first,
        second,
        third
    )
}

/**
 * Cast the current value to [T].
 * @param T The type to cast to.
 * @throws ClassCastException If the cast failed
 */
@Throws(ClassCastException::class)
inline fun <reified T> Any?.cast() = this as T

/**
 * Cast the current value to [T]. Return `null` on failure.
 * @param T The type to cast to.
 */
inline fun <reified T> Any?.castOrNull() = this as? T

/**
 * @param then The value returned if the current instance is `null`.
 * @return The current instance if not `null` else [then].
 */
fun <T : Any> T?.ifNull(then: T): T {
    return this ?: then
}

/**
 * @param block Called when the current instance is `null`.
 * @return The current instance if not `null` else the result of [block].
 */
@OptIn(ExperimentalContracts::class)
inline fun <T : Any> T?.ifNull(block: () -> T): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return this ?: block()
}

/**
 * Invokes [block] and swallows any [Throwable].
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> runSilent(block: () -> T) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    try {
        block()
    } catch (_: Throwable) {
    }
}

/**
 * Invokes [block] and returns the result or `null` if any [Throwable] was thrown.
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> runSilentAndGet(block: () -> T): T? {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return try {
        block()
    } catch (_: Throwable) {
        null
    }
}

/**
 * Same as [apply] but swallows all throwables thrown in [block].
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> T.applySilent(block: T.() -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    try {
        block()
    } catch (_: Throwable) {
    }
    return this
}

/**
 * Same as [also] but swallows all throwables thrown in [block].
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> T.alsoSilent(block: (T) -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    try {
        block(this)
    } catch (_: Throwable) {
    }
    return this
}

/**
 * Same as [to] but returns null if either the receiver or the parameter is null.
 * @param that The second element of the pair.
 * @return A [Pair] if the receiver and [that] are not null.
 */
infix fun <A, B> A?.toOrNull(that: B?): Pair<A, B>? {
    return if (this == null || that == null) {
        return null
    } else Pair(first = this, second = that)
}

/**
 * Want to run some code on another thread?
 *
 * run it with the ease of async and leave it to be executed on a Worker Thread.
 * Make sure you don't do some context related stuff in async, It may cause an memory leak
 */
@OptIn(DelicateCoroutinesApi::class)
fun async(runnable: () -> Unit) = GlobalScope.launch(Dispatchers.IO) { runnable() }

/**
 * Want to run some code on another thread?
 *
 * run it with the ease of async and leave it to be executed on a Worker Thread.
 * Make sure you don't do some context related stuff in async, It may cause an memory leak
 */
@OptIn(DelicateCoroutinesApi::class)
fun <T> async(param: T, runnable: T.() -> Unit) =
    GlobalScope.launch(Dispatchers.IO) { runnable(param) }

/**
 * Want to run some code on another thread?
 *
 * run it with the ease of asyncAwait [asyncRunnable] and leave it to be executed on a Worker Thread.
 * [awaitRunnable] wil be invoked after the asyncRunnable with the result returned from [asyncRunnable]
 * Make sure you don't do some context related stuff in [asyncRunnable], It may cause an memory leak
 */
@OptIn(DelicateCoroutinesApi::class)
fun <T> asyncAwait(asyncRunnable: () -> T?, awaitRunnable: (result: T?) -> Unit) =
    GlobalScope.launch(Dispatchers.IO) {
        asyncRunnable().let {
            launch(Dispatchers.Main) { awaitRunnable(it) }
        }
    }

/**
 * Want to run some code on another thread?
 *
 * run it with the ease of asyncAwait [asyncRunnable] and leave it to be executed on a Worker Thread. [awaitRunnable] wil be invoked after the asyncRunnable with the result returned from [asyncRunnable]
 * Make sure you don't do some context related stuff in [asyncRunnable], It may cause an memory leak
 */
@OptIn(DelicateCoroutinesApi::class)
fun <T, P> asyncAwait(param: P, asyncRunnable: P.() -> T?, awaitRunnable: (result: T?) -> Unit) =
    GlobalScope.launch(Dispatchers.IO) {
        asyncRunnable(param).let {
            launch(Dispatchers.Main) { awaitRunnable(it) }
        }
    }

/**
 * try the code in [runnable], If it runs then its perfect if its not, It won't crash your app.
 */
fun tryOrIgnore(runnable: () -> Unit) = try {
    runnable()
} catch (e: Exception) {
    e.printStackTrace()
}

/**
 * ifIs provides a block to match the value with Given Value and execute the block
 */
fun <T> T.ifIs(valueToCompare: T?, block: T.() -> Unit) {
    if (this == valueToCompare) block(this)
}

/**
 * ifIs provides a block to match the value with Given Value and execute the block
 */
fun <T> T.ifIsNot(valueToCompare: T?, block: (T) -> Unit) {
    if (this != valueToCompare) block(this)
}

/**
 * put Something In Memory to use it later
 */
fun putInMemory(key: String, any: Any?) = InMemoryCache.put(key, any)

/**
 * helper Function to Cast things Force fully
 */
inline fun <reified T> Any.forceCast() = this as T

/**
 * get Saved Data from memory, null if it os not exists
 */
fun getFromMemory(key: String): Any? = InMemoryCache.get(key)

/**
 * Check if Device is Rooted.
 */
fun isDeviceRooted(): Boolean {
    val locs = arrayOf(
        "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
        "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
        "/system/sbin/", "/usr/bin/", "/vendor/bin/"
    )
    locs.forEach {
        if ((it + "su").toFile().exists())
            return true
    }
    return false

}

/**
 * guardRun will run your code and returns True if it executed Properly else false.
 */
fun guardRun(runnable: () -> Unit): Boolean = try {
    runnable()
    true
} catch (ignore: Exception) {
    ignore.printStackTrace()
    false
}

/**
 * get Current Date.
 */
fun currentDate() = Date(System.currentTimeMillis())

/**
 * Loop with a single Int, It will call the [loop] till the value of [till]
 *
 * It will execute from 0 .. [till]
 */
fun loop(till: Int, loop: (i: Int) -> Unit) = repeat(till, loop)

/**
 * While Loop Wrapped with Kotlin Global Extension
 */
fun loopWhile(boolean: Boolean, loop: () -> Unit) {
    while (boolean) loop()
}

/**
 * Runs the Block With a Delay.
 */
fun runWithDelay(delay: Long, block: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ block() }, delay)
}

/**
 * invokes [runnable] if value is Not Null. Quite handy.
 */
fun <T : Any?> T?.isNotNull(runnable: (it: T) -> Unit) = this?.let {
    runnable(it)
}

/**
 * invokes [runnable] if value is Not Null. Quite handy.
 */
fun <T : Any?> T?.isNull(runnable: () -> Unit) {
    if (this == null) {
        runnable()
    }
}

/**
 * Run the UI Code on UI Thread From AnyWhere, No need the Activity Reference
 */
fun runOnUIThread(runnable: () -> Unit) = Handler(Looper.getMainLooper()).post(runnable)

/**
 * get CurrentTimeInMillis from System.currentTimeMillis
 */
inline val currentTimeMillis: Long get() = System.currentTimeMillis()

fun <T> T?.isNull() = this == null

fun <T> T?.isNotNull() = this != null

/**
 * ACTIONS
 */

fun <T> T?.ifNotNull(toBoolean: T.() -> Boolean) =
    if (this != null) toBoolean() else false

/**
 * Get weeks for current month and year
 *
 * @return list of week days
 */
fun getWeeks(): List<String> = getWeeks(null, null)

/**
 * Get weeks for month and year
 *
 * @param month
 * @param year
 * @return list of week days
 */
fun getWeeks(month: Int?, year: Int?): List<String> {
    val cal = Calendar.getInstance()

    val currentMonth = if (month == null) {
        cal[Calendar.MONTH]
    } else {
        cal.set(Calendar.MONTH, month)
        cal[Calendar.MONTH]
    }

    val currentYear = if (year == null) {
        cal[Calendar.YEAR]
    } else {
        cal.set(Calendar.YEAR, year)
        cal[Calendar.YEAR]
    }

    cal[Calendar.DAY_OF_MONTH] = 1

    val weekRanges: MutableList<String> = ArrayList()

    while (cal[Calendar.MONTH] == currentMonth) {
        val year = cal[Calendar.YEAR]
        val week = cal[Calendar.WEEK_OF_MONTH]
        val dayOfWeek = cal[Calendar.DAY_OF_WEEK]

        // Only consider days in the current month
        if (cal[Calendar.MONTH] == currentMonth && cal[Calendar.YEAR] == currentYear) {
            val startDay = cal[Calendar.DAY_OF_MONTH]
            var endDay = startDay + (7 - dayOfWeek)

            if (endDay > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            }

            weekRanges.add(String.format("%d-%d", startDay, endDay))
        }

        cal.add(Calendar.DAY_OF_MONTH, 7 - dayOfWeek + 1)
    }

    println(weekRanges)
    return weekRanges
}
