package com.gurpster.octopus.extensions

import android.content.SharedPreferences
import androidx.annotation.CheckResult

/**
 * Calls [SharedPreferences.registerOnSharedPreferenceChangeListener]
 * when [register] is `true` and [SharedPreferences.unregisterOnSharedPreferenceChangeListener]
 * when it is `false`
 * @param listener The listener to register or unregister.
 * @param register `true` to register the listener. `false` to unregister the listener.
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : SharedPreferences> T.registerOnChangeListener(
    listener: SharedPreferences.OnSharedPreferenceChangeListener,
    register: Boolean = true
): T {
    if (register) {
        registerOnSharedPreferenceChangeListener(listener)
    } else {
        unregisterOnSharedPreferenceChangeListener(listener)
    }
    return this
}

/**
 * The same as calling [SharedPreferences.edit], [Editor.putBoolean] and than [Editor.apply].
 * For consecutive calls, use the editor [Editor], as this is more efficient in this case.
 * @param key The name of the preference to modify.
 * @param value The new value for the preference.
 */
fun SharedPreferences.put(key: String, value: Boolean) {
    edit().putBoolean(key, value).apply()
}

/**
 * The same as calling [SharedPreferences.edit], [Editor.putFloat] and than [Editor.apply].
 * For consecutive calls, use the editor [Editor], as this is more efficient in this case.
 * @param key The name of the preference to modify.
 * @param value The new value for the preference.
 */
fun SharedPreferences.put(key: String, value: Float) {
    edit().putFloat(key, value).apply()
}

/**
 * The same as calling [SharedPreferences.edit], [Editor.putInt] and than [Editor.apply].
 * For consecutive calls, use the editor [Editor], as this is more efficient in this case.
 * @param key The name of the preference to modify.
 * @param value The new value for the preference.
 */
fun SharedPreferences.put(key: String, value: Int) {
    edit().putInt(key, value).apply()
}

/**
 * The same as calling [SharedPreferences.edit], [Editor.putLong] and than [Editor.apply].
 * For consecutive calls, use the editor [Editor], as this is more efficient in this case.
 * @param key The name of the preference to modify.
 * @param value The new value for the preference.
 */
fun SharedPreferences.put(key: String, value: Long) {
    edit().putLong(key, value).apply()
}

/**
 * The same as calling [SharedPreferences.edit], [Editor.putString] and than [Editor.apply].
 * For consecutive calls, use the editor [Editor], as this is more efficient in this case.
 * @param key The name of the preference to modify.
 * @param value The new value for the preference.
 */
fun SharedPreferences.put(key: String, value: String) {
    edit().putString(key, value).apply()
}

/**
 * The same as calling [SharedPreferences.edit], [Editor.putStringSet] and than [Editor.apply].
 * For consecutive calls, use the editor [Editor], as this is more efficient in this case.
 * @param key The name of the preference to modify.
 * @param value The new value for the preference.
 */
fun SharedPreferences.put(key: String, value: Set<String>) {
    edit().putStringSet(key, value).apply()
}

/**
 * The same as calling [SharedPreferences.edit], [Editor.remove] and than [Editor.apply].
 * For consecutive calls, use the editor [Editor], as this is more efficient in this case.
 * @param key The name of the preference to remove.
 */
fun SharedPreferences.remove(key: String) {
    edit().remove(key).apply()
}

/**
 * Removes all values from the receiver.
 * The same as calling [SharedPreferences.edit], [Editor.clear] and than [Editor.apply].
 * For consecutive calls, use the editor [Editor], as this is more efficient in this case.
 */
fun SharedPreferences.clear() {
    edit().clear().apply()
}

/**
 * The number of key value paris in the receiver.
 */
@get:CheckResult
inline val SharedPreferences.size: Int get() = all.size

/**
 * @param key The name of the preference to retrieve.
 * @return The preference value if it exists and is from type [Boolean]. `null` otherwise.
 */
@CheckResult
fun SharedPreferences.getBooleanOrNull(key: String) = all[key] as? Boolean

/**
 * @param key The name of the preference to retrieve.
 * @return The preference value if it exists and is from type [Float]. `null` otherwise.
 */
@CheckResult
fun SharedPreferences.getFloatOrNull(key: String) = all[key] as? Float

/**
 * @param key The name of the preference to retrieve.
 * @return The preference value if it exists and is from type [Int]. `null` otherwise.
 */
@CheckResult
fun SharedPreferences.getIntOrNull(key: String) = all[key] as? Int

/**
 * @param key The name of the preference to retrieve.
 * @return The preference value if it exists and is from type [Long]. `null` otherwise.
 */
@CheckResult
fun SharedPreferences.getLongOrNull(key: String) = all[key] as? Long

/**
 * @param key The name of the preference to retrieve.
 * @return The preference value if it exists and is from type [String]. `null` otherwise.
 */
@CheckResult
fun SharedPreferences.getStringOrNull(key: String) = all[key] as? String

/**
 * @param key The name of the preference to retrieve.
 * @param defValue Value to return if this preference does not exist or is no StringSet.
 * @return The preference value if it exists and is a Set<String>. The [defValue] otherwise.
 * ### The exact class of the returned Set is undefined.
 * In any case do not make any changes to the set.
 * Use [getMutableStringSet] to get a copy of the internal set.
 */
@CheckResult
fun SharedPreferences.getImmutableStringSet(
    key: String,
    defValue: Set<String>
): Set<String> {
    @Suppress("UNCHECKED_CAST")
    return all[key] as? Set<String> ?: defValue
}

/**
 * @param key The name of the preference to retrieve.
 * @param defValue Value to return if this preference does not exist or is no StringSet.
 * @return The preference value if it exists and is a Set<String>. The [defValue] otherwise.
 * The returned set is not the original one (except if it is the fallback) and can be modified.
 */
@CheckResult
fun SharedPreferences.getMutableStringSet(
    key: String,
    defValue: MutableSet<String>
): MutableSet<String> {
    @Suppress("UNCHECKED_CAST")
    return HashSet(all[key] as? Set<String> ?: return defValue)
}

/**
 * @param key The name of the preference to retrieve.
 * @return The preference value if it exists and is a Set<String>. `null` otherwise.
 * ### The exact class of the returned Set is undefined. In any case do not make any changes to the set. Use [getMutableStringSetOrNull] to get a copy of the internal set.
 */
@CheckResult
@Suppress("UNCHECKED_CAST")
fun SharedPreferences.getImmutableStringSetOrNull(key: String) = all[key] as? Set<String>

/**
 * @param key The name of the preference to retrieve.
 * @return The preference value if it exists and is a Set<String>. `null` otherwise.
 * The returned set is not the original one and can be modified.
 */
@CheckResult
fun SharedPreferences.getMutableStringSetOrNull(key: String): MutableSet<String>? {
    @Suppress("UNCHECKED_CAST")
    val set = all[key] as? Set<String>
    return if (set != null) HashSet(set) else null
}

/**
 * Invert the value for [key] if the value is not `null` and from type boolean.
 * @param key The name of the preference to modify.
 * @return The new value of [key]. `null` if no value is assigned to the [key].
 */
@CheckResult
fun SharedPreferences.invertBoolean(key: String): Boolean? {
    return getBooleanOrNull(key)?.not()?.also { newValue ->
        put(key, newValue)
    }
}

/**
 * Invert the value for [key] if the value is not `null` and from type boolean.
 * Otherwise the [defaultValue] is set for the [key].
 * @param key The name of the preference to modify.
 * @param defaultValue This value is set if currently no boolean is set for [key].
 * @return The new value of [key].
 */
@CheckResult
fun SharedPreferences.invertBoolean(key: String, defaultValue: Boolean): Boolean {
    val newValue = getBooleanOrNull(key)?.not() ?: defaultValue
    put(key, newValue)
    return newValue
}