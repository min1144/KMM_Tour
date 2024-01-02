package com.test.examplekmp.presentation.base

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.test.examplekmp.presentation.base.root.LocalComponentContext
import com.test.examplekmp.presentation.util.log.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.LocalKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

abstract class BaseViewModel : ViewModel() {

    protected val logger
        get() = Logger.viewmodel

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        defaultJob {
            handleException(throwable)
        }
    }

    private val defaultDispatchers =
        SupervisorJob() + Dispatchers.Default + coroutineExceptionHandler

    private val mainDispatchers = SupervisorJob() + Dispatchers.Main + coroutineExceptionHandler

    private val ioDispatchers = SupervisorJob() + Dispatchers.IO + coroutineExceptionHandler

    private val _exceptionError = MutableSharedFlow<Throwable>()
    val error: SharedFlow<Throwable> get() = _exceptionError.asSharedFlow()

    private val _loading = MutableStateFlow(false)

    val loading: StateFlow<Boolean>
        get() = _loading.asStateFlow()

    fun setLoading(loading: Boolean) = defaultJob {
        _loading.emit(loading)
    }

    protected open fun onError(throwable: Throwable): Boolean {
        return true
    }

    protected fun uiJob(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(mainDispatchers) {
            block()
        }

    protected fun ioJob(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(ioDispatchers) {
            block()
        }

    protected suspend fun withIoJob(block: suspend CoroutineScope.() -> Unit) =
        withContext(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                handleException(e)
            }
        }

    protected fun defaultJob(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(defaultDispatchers) {
            block()
        }

    protected suspend fun withDefaultJob(block: suspend CoroutineScope.() -> Unit) =
        withContext(Dispatchers.Default) {
            try {
                block()
            } catch (e: Exception) {
                handleException(e)
            }
        }

    protected suspend fun handleException(throwable: Throwable) {
        logD("coroutineExceptionHandler error > $throwable")
        if (onError(throwable))
            _exceptionError.emit(throwable)
    }

    override fun onCleared() {
        super.onCleared()
        logD("onCleared")
    }

    private fun logD(message : String){
        logger.d("${this::class.qualifiedName} $message")
    }
}

@Composable
inline fun <reified T : BaseViewModel> bindViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val store = LocalViewModelStore.current
    val scope = LocalKoinScope.current
    val (viewModelStoreKey, viewModel) = getViewModel<T>(
        store,
        scope,
        qualifier,
        parameters
    )
    Logger.viewmodel.d("bindViewModel viewModelStoreKey:$viewModelStoreKey, viewModel:${viewModel.hashCode()}")
    val context = LocalComponentContext.current

    context.lifecycle.doOnDestroy {
        store.clear(viewModelStoreKey)
        Logger.viewmodel.d("bindViewModel doOnDestroy : ${viewModelStoreKey} v:${viewModel.hashCode()}")
    }
    return viewModel
}

