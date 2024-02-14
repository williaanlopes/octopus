package com.gurpster.octopus.extensions

import android.content.IntentFilter

/**
 * Add the given [actions] to the intent filter.
 * @param actions The actions to add.
 * @return The receiver object, for chaining multiple calls into a single statement.
 * @see IntentFilter.addAction
 */
fun IntentFilter.addActions(vararg actions: String): IntentFilter {
    for (action in actions) {
        addAction(action)
    }
    return this
}

/**
 * Add the given [categories] to the intent filter.
 * @param categories The categories to add.
 * @return The receiver object, for chaining multiple calls into a single statement.
 * @see IntentFilter.addCategory
 */
fun IntentFilter.addCategories(vararg categories: String): IntentFilter {
    for (category in categories) {
        addCategory(category)
    }
    return this
}

/**
 * Add the given [dataSchemes] to the intent filter.
 * @param dataSchemes The data scheme to add.
 * @return The receiver object, for chaining multiple calls into a single statement.
 * @see IntentFilter.addDataScheme
 */
fun IntentFilter.addDataSchemes(vararg dataSchemes: String): IntentFilter {
    for (dataScheme in dataSchemes) {
        addDataScheme(dataScheme)
    }
    return this
}

/**
 * Add the given [dataTypes] to the intent filter.
 * @param dataTypes The data types to add.
 * @return The receiver object, for chaining multiple calls into a single statement.
 * @throws IntentFilter.MalformedMimeTypeException If any of the given MIME types is
 * syntactically incorrect.
 * @see IntentFilter.addDataType
 */
@Throws(IntentFilter.MalformedMimeTypeException::class)
fun IntentFilter.addDataTypes(vararg dataTypes: String): IntentFilter {
    for (dataType in dataTypes) {
        addDataType(dataType)
    }
    return this
}