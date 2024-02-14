package com.gurpster.octopus.extensions

import android.util.Log
import android.util.Log.wtf
import androidx.annotation.CheckResult
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

/**
 * Write a message with level [Log.VERBOSE] to the logcat's main buffer.
 * Note: This method can not be inlined because [KT-8628](https://youtrack.jetbrains.com/issue/KT-8628)
 * In addition, the line number determined via [generateLogTag] is incorrect
 * if the calling function is an inline function.
 * @param message The log message.
 * @param tag The tag of the message.
 */
fun logVerbose(message: String, tag: String = generateLogTag()) {
    Timber.v(tag, message)
}

/**
 * Write a message with level [Log.DEBUG] to the logcat's main buffer.
 * Note: This method can not be inlined because [KT-8628](https://youtrack.jetbrains.com/issue/KT-8628)
 * In addition, the line number determined via [generateLogTag] is incorrect
 * if the calling function is an inline function.
 * @param message The log message.
 * @param tag The tag of the message.
 */
fun logDebug(message: String, tag: String = generateLogTag()) {
    Timber.d(tag, message)
}

/**
 * Write a message with level [Log.INFO] to the logcat's main buffer.
 * Note: This method can not be inlined because [KT-8628](https://youtrack.jetbrains.com/issue/KT-8628)
 * In addition, the line number determined via [generateLogTag] is incorrect
 * if the calling function is an inline function.
 * @param message The log message.
 * @param tag The tag of the message.
 */
fun logInfo(message: String, tag: String = generateLogTag()) {
    Timber.i(tag, message)
}

/**
 * Write a message with level [Log.WARN] to the logcat's main buffer.
 * Note: This method can not be inlined because [KT-8628](https://youtrack.jetbrains.com/issue/KT-8628)
 * In addition, the line number determined via [generateLogTag] is incorrect
 * if the calling function is an inline function.
 * @param message The log message.
 * @param tag The tag of the message.
 */
fun logWarn(message: String, tag: String = generateLogTag()) {
    Timber.w(tag, message)
}

/**
 * Write a message with level [Log.ERROR] to the logcat's main buffer.
 * Note: This method can not be inlined because [KT-8628](https://youtrack.jetbrains.com/issue/KT-8628)
 * In addition, the line number determined via [generateLogTag] is incorrect
 * if the calling function is an inline function.
 * @param message The log message.
 * @param tag The tag of the message.
 */
fun logError(message: String, tag: String = generateLogTag()) {
    Timber.e(tag, message)
}

/**
 * Write a message with level [Log.ASSERT] to the logcat's main buffer.
 * Note: This method can not be inlined because [KT-8628](https://youtrack.jetbrains.com/issue/KT-8628)
 * In addition, the line number determined via [generateLogTag] is incorrect
 * if the calling function is an inline function.
 * @param message The log message.
 * @param tag The tag of the message.
 */
fun logFatal(message: String, tag: String = generateLogTag()) {
    Log.println(Log.ASSERT, tag, message)
}

/**
 * Log the Json into the Logcat as a DEBUG Log
 */
fun logJson(json: Any?, tag: String? = null) {
    Timber.log(
        Log.DEBUG,
        tag,
        formatJson(if (json == null) "null" else "" + json)
    )
}

/**
 * Note: This method can not be inlined because [KT-8628](https://youtrack.jetbrains.com/issue/KT-8628)
 * In addition, the determined line number is incorrect if called from an inline function.
 * @return A string suitable as tag for logcat entries.
 * The string consists of the file name and the line in which this method was called.
 */
@CheckResult
@PublishedApi
internal fun generateLogTag(): String {
    // We have to ignore the first four elements, the first two are internal,
    // the third is this method and the fourth is the log method that called this method.
    val st = Thread.currentThread().stackTrace[4]
    val line = ":${st.lineNumber}"
    return if (st.fileName.length > (23 - line.length)) {
        st.fileName.take(21 - line.length) + ".." + line

    } else st.fileName + line
}

private fun formatJson(json: String): String {
    return try {
        val trimJson = json.trim()
        when {
            trimJson.startsWith("{") -> JSONObject(trimJson).toString(2)
            trimJson.startsWith("[") -> JSONArray(trimJson).toString(2)
            else -> trimJson
        }
    } catch (e: JSONException) {
        Timber.wtf(e)
        "An Error While Printing This Json. Please Check Above CrashLog"
    }

}

fun Timber.json(json: Any?, tag: String? = null) {
    Timber.log(
        Log.DEBUG,
        tag,
        formatJson(if (json == null) "null" else "" + json)
    )
}