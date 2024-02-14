package com.gurpster.location.extensions

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

val compositeDisposable = CompositeDisposable()

fun <T : Any> Observable<T>.baseSubscribe(
    observeOn: Scheduler? = AndroidSchedulers.mainThread(),
    subscribeOn: Scheduler = Schedulers.io(),
    onError: ((Throwable) -> Unit)? = null,
    onSuccess: (T) -> Unit
) {
    this.subscribeOn(subscribeOn)
        .run {
            if (observeOn != null) {
                observeOn(observeOn)
            } else {
                this
            }
        }
        .subscribe(
            { onSuccess.invoke(it) },
            { onError?.invoke(it) }
        ).addToDisposable()
}

fun <T : Any> Single<T>.baseSubscribe(
    observeOn: Scheduler? = AndroidSchedulers.mainThread(),
    subscribeOn: Scheduler = Schedulers.io(),
    onError: ((Throwable) -> Unit)? = null,
    onSuccess: (T) -> Unit
) {
    this.subscribeOn(subscribeOn)
        .run {
            if (observeOn != null) {
                observeOn(observeOn)
            } else {
                this
            }
        }
        .subscribe(
            { onSuccess.invoke(it) },
            { onError?.invoke(it) }
        ).addToDisposable()
}

fun <T : Any> Flowable<T>.baseSubscribe(
    observeOn: Scheduler? = AndroidSchedulers.mainThread(),
    subscribeOn: Scheduler = Schedulers.io(),
    onError: ((Throwable) -> Unit)? = null,
    onSuccess: (T) -> Unit
) {
    this.subscribeOn(subscribeOn)
        .run {
            if (observeOn != null) {
                observeOn(observeOn)
            } else {
                this
            }
        }
        .subscribe(
            { onSuccess.invoke(it) },
            { onError?.invoke(it) }
        ).addToDisposable()
}

fun Completable.baseSubscribe(
    observeOn: Scheduler? = AndroidSchedulers.mainThread(),
    subscribeOn: Scheduler = Schedulers.io(),
    onError: ((Throwable) -> Unit)? = null,
    onComplete: () -> Unit
) {
    this.subscribeOn(subscribeOn)
        .run {
            if (observeOn != null) {
                observeOn(observeOn)
            } else {
                this
            }
        }
        .subscribe(
            { onComplete.invoke() },
            { onError?.invoke(it) }
        ).addToDisposable()
}

fun Disposable.addToDisposable() {
    compositeDisposable.add(this)
}