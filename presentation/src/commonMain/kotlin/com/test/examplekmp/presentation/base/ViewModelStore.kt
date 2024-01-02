package com.test.examplekmp.presentation.base

import androidx.compose.runtime.staticCompositionLocalOf
import com.test.examplekmp.presentation.util.log.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

class ViewModelStore {

    private val map = hashMapOf<String, ViewModel>()

    private val lock = SynchronizedObject()

    fun get(key: String, factory: () -> ViewModel): ViewModel {
        synchronized(lock) {
            return map.getOrPut(key, factory)
        }
    }

    fun get(key: String): ViewModel? {
        synchronized(lock) {
            return map[key]
        }
    }

    fun clear(key: String) {
        synchronized(lock) {
            val old = map[key]
            if (old != null) {
                old.onCleared()
                map.remove(key)
            }
            Logger.viewmodel.d("clear viewModel $key, contains: ${map.containsKey(key)}")
        }
    }

    fun clear() {
        synchronized(lock) {
            for (vm in map.values) {
                vm.onCleared()
            }
            map.clear()
        }
    }
}

inline fun <reified T : BaseViewModel> getViewModel(
    store: ViewModelStore,
    scope: Scope,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): Pair<String, T> {
    val viewModelStoreKey = T::class.qualifiedName.orEmpty()
    val v = store.get(viewModelStoreKey) {
        val out: T = scope.get(qualifier, parameters)
        out
    } as T
    return viewModelStoreKey to v
}

val LocalViewModelStore = staticCompositionLocalOf<ViewModelStore> {
    error("ViewModelStore not provided")
}