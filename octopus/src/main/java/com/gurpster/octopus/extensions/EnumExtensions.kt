package com.gurpster.octopus.extensions

/**
 * The enum entry with the given [name]. `null` if no enum with the given name exists in [T].
 */
inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? {
    return try {
        enumValueOf<T>(name = name)
    } catch (e: IllegalArgumentException) {
        null
    }
}

inline fun <reified T : Enum<T>> find(predicate: (item: T) -> Boolean): T? =
    enumValues<T>().find { predicate(it) }

inline fun <reified T : Enum<T>> first(predicate: (item: T) -> Boolean): T =
    enumValues<T>().first { predicate(it) }

inline fun <reified T : Enum<T>> convert(ord: Int): T = enumValues<T>()[ord]

/**
 * Returns an enum entry with specified name.
 */
inline fun <reified E : Enum<E>> enumValueOfOrNull(name: String?): E? {
    return try {
        enumValueOf<E>(name!!)
    } catch (e: IllegalArgumentException) {
        null
    } catch (e: NullPointerException) {
        null
    }
}

/**
 * Returns an enum entry with specified name.
 */
inline fun <reified T : Enum<T>> String.enumSafeValueOf(): T? {
    return try {
        enumValueOf<T>(this)
    } catch (e: Exception) {
        null
    }
}