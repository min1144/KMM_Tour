package com.test.examplekmp.presentation.ui.map

import com.arkivanov.decompose.ComponentContext

class DefaultMapComponent(
    private val componentContext: ComponentContext,
    private val onItemClicked: (contentId: String, contentTypeId: String) -> Unit,
) : MapComponent, ComponentContext by componentContext {

    override fun onItemClick(contentId: String, contentTypeId: String) {
        onItemClicked(contentId, contentTypeId)
    }
}