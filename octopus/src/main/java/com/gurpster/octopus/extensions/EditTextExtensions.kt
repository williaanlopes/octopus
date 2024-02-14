package com.gurpster.octopus.extensions

import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import androidx.annotation.CheckResult
import androidx.core.widget.doOnTextChanged
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import timber.log.Timber
import kotlin.coroutines.EmptyCoroutineContext

internal fun checkMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }
}

fun EditText.getString() = text.toString()

fun TextInputEditText.string(): String {
    return when {
        TextUtils.isEmpty(this.text) -> ""
        else -> return this.text.toString()
    }
}

fun TextInputEditText.getOnlyNumbers(): Int {
    return this.text.toString().trim().onlyNumbers
}

fun EditText.getText(): String {
    return when {
        TextUtils.isEmpty(this.text) -> ""
        else -> return this.text.toString()
    }
}

fun EditText.getTextNumbers(): Int {
    return this.text.toString().trim().onlyNumbers
}

@CheckResult
fun EditText.firstChange(): Flow<Unit> =
    callbackFlow {
        checkMainThread()

        val listener = doOnTextChanged { _, _, _, _ -> trySend(Unit) }

        awaitClose {
            Dispatchers.Main.dispatch(EmptyCoroutineContext) {
                removeTextChangedListener(listener)
                Timber.d("removeTextChangedListener $listener $this")
            }
        }

    }.take(1)

/**
 * Text changes
 *
 * Usage:
 *
 * editText.textChanges()
 *     .filterNot { it.isNullOrBlank() }
 *     .debounce(300)
 *     .distinctUntilChanged()
 *     .flatMapLatest { executeSearch(it) }
 *     .onEach { updateUI(it) }
 *     .launchIn(lifecycleScope)
 *
 * @return
 */
@CheckResult
fun EditText.textChanges(): Flow<CharSequence?> = callbackFlow {
    checkMainThread()

    val listener = doOnTextChanged { text, _, _, _ ->
        trySend(text)
    }

    awaitClose {
        removeTextChangedListener(listener)
    }

}.onStart { emit(text) }

/**
 * Set the cursor to the start of the text.
 * @param T The type of the EditText.
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : EditText> T.setCursorToStart(): T {
    setSelection(0)
    return this
}

/**
 * Set the cursor to the end of the text.
 * @param T The type of the EditText.
 * @return The receiver object, for chaining multiple calls.
 */
fun <T : EditText> T.setCursorToEnd(): T {
    setSelection(text.length)
    return this
}