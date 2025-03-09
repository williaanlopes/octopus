package com.gurpster.octopus.extensions

fun <T> Collection<T>?.orEmpty(): Collection<T> = this ?: emptyList()

val <T> Collection<T>.lastIndex: Int
    get() = size - 1

/**
 * @return `null` if the collection is empty or `null`. Otherwise the collection is returned.
 */
fun <T> Collection<T>.emptyAsNull(): Collection<T>? = ifEmpty { null }

/**
 * @return A list of all entries in [elements] that are of type [T].
 */
inline fun <reified T : Any> listOfInstanceOf(vararg elements: Any?): List<T> {
    return elements.filterIsInstance(T::class.java)
}

/**
 * @return A list containing [element] it it is of type [T]. Otherwise an empty list is returned.
 */
inline fun <reified T : Any> listOfInstanceOf(element: Any?): List<T> {
    return if (element is T) listOf(element) else emptyList()
}

/**
 * Remove or add an element based on the value of [add].
 * @param add `true` to add the element, `false` to remove the element.
 * @param element The element.
 * @return `true` if the collection was changed, `false` if the collection is unchanged.
 */
fun <T> MutableCollection<T>.addOrRemove(add: Boolean, element: T): Boolean {
    return if (add) add(element) else remove(element)
}

/**
 * Adds the element to the collection if it is not null.
 * @param element The element to add.
 * @return `true` if the element has been added, `false` if the element was null or the
 * collection does not support duplicates and the element is already contained in the collection.
 */
fun <T> MutableCollection<T>.addNotNull(element: T?): Boolean {
    return add(element = element ?: return false)
}

/**
 * Adds all elements to the collection that are not null.
 * @param elements The elements to add.
 * @return `true` if any of the specified elements was added to the collection,
 * `false` if the collection was not modified.
 */
fun <T> MutableCollection<T>.addAllNotNull(elements: Iterable<T?>): Boolean {
    var added = false
    for (item in elements)
        if (item != null && add(item)) added = true
    return added
}

inline fun <reified E> Array<E>?.safe(): Array<E> =
    this ?: emptyArray()

fun <E> List<E>?.safe(default: List<E> = emptyList()): List<E> = this ?: default

fun <E> List<E>?.safeMutable(default: MutableList<E> = mutableListOf()): MutableList<E> =
    (if (this is MutableList<E>) this else this?.toMutableList()) ?: default

fun <K, V> Map<K, V>?.safeMutable(default: MutableMap<K, V> = mutableMapOf()): MutableMap<K, V> =
    (if (this is MutableMap<K, V>) this else this?.toMutableMap()) ?: default

fun <E> List<E>?.isNotNullOrEmpty() = this != null && isNotEmpty()

fun <E> List<E>?.isNullOrEmpty(): Boolean = this == null || isEmpty()