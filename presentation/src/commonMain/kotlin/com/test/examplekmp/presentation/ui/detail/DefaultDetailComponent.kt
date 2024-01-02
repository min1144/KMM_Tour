package com.test.examplekmp.presentation.ui.detail

class DefaultDetailComponent(
    private val onBackClick: () -> Unit
) : DetailComponent{

    override fun onBackClick() {
        onBackClick.invoke()
    }
}