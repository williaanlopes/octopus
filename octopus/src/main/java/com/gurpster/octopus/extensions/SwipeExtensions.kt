package com.gurpster.octopus.extensions

import androidx.annotation.CheckResult
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@CheckResult
fun SwipeRefreshLayout.refreshes(): Flow<Unit> = callbackFlow {
    checkMainThread()

    setOnRefreshListener { trySend(Unit) }
    awaitClose { setOnRefreshListener(null) }
}