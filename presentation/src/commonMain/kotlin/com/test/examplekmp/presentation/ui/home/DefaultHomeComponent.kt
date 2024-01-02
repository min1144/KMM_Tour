package com.test.examplekmp.presentation.ui.home

import com.arkivanov.decompose.ComponentContext

class DefaultHomeComponent(
    private val componentContext: ComponentContext,
    private val onItemClicked: (contentId: String, contentTypeId: String) -> Unit,
) : HomeComponent, ComponentContext by componentContext {

    override fun onItemClick(contentId: String, contentTypeId: String) {
        onItemClicked(contentId, contentTypeId)
    }
}