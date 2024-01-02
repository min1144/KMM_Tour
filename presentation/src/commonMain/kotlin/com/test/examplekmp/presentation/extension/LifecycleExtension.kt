package com.test.examplekmp.presentation.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

enum class LifeCycleEvent {

    ON_CREATE,

    ON_START,

    ON_RESUME,

    ON_PAUSE,

    ON_STOP,

    ON_DESTROY,

    ON_ANY;
}

suspend fun Lifecycle.repeatOnLifecycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) {
    require(state !== Lifecycle.State.INITIALIZED) {
        "repeatOnLifecycle cannot start work with the INITIALIZED lifecycle state."
    }

    if (state === Lifecycle.State.DESTROYED) {
        return
    }

    // This scope is required to preserve context before we move to Dispatchers.Main
    coroutineScope {
        withContext(Dispatchers.Main.immediate) {
            // Check the current state of the lifecycle as the previous check is not guaranteed
            // to be done on the main thread.
            if (state === Lifecycle.State.DESTROYED) return@withContext

            // Instance of the running repeating coroutine
            var launchedJob: Job? = null

            // Registered observer
            var observer: Lifecycle.Callbacks? = null
            try {
                // Suspend the coroutine until the lifecycle is destroyed or
                // the coroutine is cancelled
                suspendCancellableCoroutine<Unit> { cont ->
                    // Lifecycle observers that executes `block` when the lifecycle reaches certain state, and
                    // cancels when it falls below that state.
                    val startWorkEvent = upTo(state)
                    val cancelWorkEvent = downFrom(state)
                    val mutex = Mutex()
                    observer = LifecycleEventObserver { event ->
                        if (event == startWorkEvent) {
                            // Launch the repeating work preserving the calling context
                            launchedJob = this@coroutineScope.launch {
                                // Mutex makes invocations run serially,
                                // coroutineScope ensures all child coroutines finish
                                mutex.withLock {
                                    coroutineScope {
                                        block()
                                    }
                                }
                            }
                            return@LifecycleEventObserver
                        }
                        if (event == cancelWorkEvent) {
                            launchedJob?.cancel()
                            launchedJob = null
                        }
                        if (event == LifeCycleEvent.ON_DESTROY) {
                            cont.resume(Unit)
                        }
                    }

                    observer?.let {
                        this@repeatOnLifecycle.subscribe(it)
                        if (state == Lifecycle.State.STARTED) {
                            it.onStart()
                        }
                    }
                }
            } finally {
                launchedJob?.cancel()
                observer?.let {
                    this@repeatOnLifecycle.unsubscribe(it)
                }
            }
        }
    }
}

@Composable
fun LifecycleOwner.OnLifeCycle(
    block: suspend CoroutineScope.(event: LifeCycleEvent) -> Unit
) {
    val o = LifecycleEventObserver {
        CoroutineScope(Dispatchers.Unconfined).launch {
            block.invoke(this, it)
        }
    }

    lifecycle.subscribe(o)
    DisposableEffect(this) {
        onDispose {
            this@OnLifeCycle.lifecycle.unsubscribe(o)
        }
    }
}

fun LifecycleEventObserver(
    block: (event: LifeCycleEvent) -> Unit
): Lifecycle.Callbacks = object : Lifecycle.Callbacks {

    private var prevEvent = LifeCycleEvent.ON_ANY

    override fun onCreate() {
        if (prevEvent == LifeCycleEvent.ON_ANY) {
            LifeCycleEvent.ON_CREATE
        } else {
            LifeCycleEvent.ON_STOP
        }.run { apply(this) }
    }

    override fun onDestroy() {
        apply(LifeCycleEvent.ON_DESTROY)
    }

    override fun onPause() {
        apply(LifeCycleEvent.ON_PAUSE)
    }

    override fun onResume() {
        apply(LifeCycleEvent.ON_RESUME)
    }

    override fun onStart() {
        apply(LifeCycleEvent.ON_START)
    }

    private fun apply(event: LifeCycleEvent) = with(event) {
        block.invoke(this)
        prevEvent = this
    }
}

private fun downFrom(state: Lifecycle.State): LifeCycleEvent? = when (state) {
    Lifecycle.State.CREATED -> LifeCycleEvent.ON_DESTROY
    Lifecycle.State.STARTED -> LifeCycleEvent.ON_STOP
    Lifecycle.State.RESUMED -> LifeCycleEvent.ON_PAUSE
    else -> null
}

private fun upTo(state: Lifecycle.State): LifeCycleEvent? = when (state) {
    Lifecycle.State.CREATED -> LifeCycleEvent.ON_CREATE
    Lifecycle.State.STARTED -> LifeCycleEvent.ON_START
    Lifecycle.State.RESUMED -> LifeCycleEvent.ON_RESUME
    else -> null
}
