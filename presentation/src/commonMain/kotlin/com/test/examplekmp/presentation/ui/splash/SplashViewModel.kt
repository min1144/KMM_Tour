package com.test.examplekmp.presentation.ui.splash

import com.test.examplekmp.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplashViewModel: BaseViewModel() {

    private val _isNext = MutableStateFlow(false)

    val isNext: StateFlow<Boolean>
        get() =  _isNext

    init {
        moveMain()
    }

    private fun moveMain() {
        uiJob {
            delay(1000L)
            _isNext.emit(true)
        }
    }
}