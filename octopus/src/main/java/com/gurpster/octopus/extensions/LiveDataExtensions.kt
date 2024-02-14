package com.gurpster.octopus.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun <T : Any> mutableLivedataOf(initialValue: T? = null): MutableLiveData<T> {
    return MutableLiveData<T>().apply { value = initialValue }
}

inline fun <T> LiveData<T>.filter(crossinline predicate: (T?) -> Boolean): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    mediator.addSource(this) {
        if (predicate(it)) {
            mediator.value = it
        }
    }

    return mediator
}

fun <T> LiveData<T>.take(count: Int): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    var taken = 0
    mediator.addSource(this) {
        if (taken < count) {
            mediator.value = it
            taken++
        } else {
            mediator.removeSource(this)
        }
    }

    return mediator
}

inline fun <T> LiveData<T>.takeUntil(crossinline predicate: (T?) -> Boolean): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    mediator.addSource(this) {
        if (predicate(it)) {
            mediator.value = it
        } else {
            mediator.removeSource(this)
        }
    }

    return mediator
}


fun <A, B, Result> LiveData<A>.combineLatest(
    other: LiveData<B>,
    combiner: (A, B) -> Result
): LiveData<Result> {
    val mediator = MediatorLiveData<Result>()
    mediator.addSource(this) { a ->
        val b = other.value
        if (b != null) {
            mediator.value = combiner(a, b)
        }
    }
    mediator.addSource(other) { b ->
        val a = this@combineLatest.value
        if (a != null) {
            mediator.value = combiner(a, b)
        }
    }
    return mediator
}