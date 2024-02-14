package com.gurpster.octopus.extensions

import android.widget.SearchView
import androidx.annotation.CheckResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

data class SearchViewQueryTextEvent(
    val view: SearchView,
    val query: CharSequence,
    val isSubmitted: Boolean,
)

@CheckResult
fun SearchView.queryTextEvents(): Flow<SearchViewQueryTextEvent> =
    callbackFlow {
        checkMainThread()

        setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    trySend(
                        SearchViewQueryTextEvent(
                            view = this@queryTextEvents,
                            query = query,
                            isSubmitted = true,
                        ),
                    )
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    trySend(
                        SearchViewQueryTextEvent(
                            view = this@queryTextEvents,
                            query = newText,
                            isSubmitted = false,
                        ),
                    )
                    return true
                }
            },
        )

        awaitClose {
            setOnQueryTextListener(null)
        }

    }.onStart {
        emit(
            SearchViewQueryTextEvent(
                view = this@queryTextEvents,
                query = query,
                isSubmitted = false,
            )
        )
    }