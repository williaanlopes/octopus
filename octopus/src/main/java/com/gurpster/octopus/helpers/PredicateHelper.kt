package com.gurpster.octopus.helpers

object PredicateHelper {

    inline val ALWAYS_TRUE: (Any?) -> Boolean get() = { true }

    inline val ALWAYS_FALSE: (Any?) -> Boolean get() = { false }

}