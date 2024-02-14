package com.gurpster.octopus.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Prints the stack trace of the exception if the result is [Result.isFailure] to the standard
 * error output.
 * @return The receiver object, for chaining multiple calls.
 */
fun <T> Result<T>.printStackTraceOnFailure(): Result<T> {
    exceptionOrNull()?.printStackTrace()
    return this
}

/**
 * @param transform The method to transform the encapsulated [Throwable].
 * @return Transforms the encapsulated throwable of the current Result into a new Result
 * using the [transform] function if it is [failure][Result.isFailure],
 * otherwise it returns the current Result unchanged.
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.mapFailure(transform: (Throwable) -> Throwable): Result<T> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return Result.failure(transform(exceptionOrNull() ?: return this))
}

/**
 * @return Same as [Result.getOrNull].
 */
operator fun <T> Result<T>.component1(): T? = getOrNull()

/**
 * @return Same as [Result.exceptionOrNull].
 */
operator fun <T> Result<T>.component2(): Throwable? = exceptionOrNull()