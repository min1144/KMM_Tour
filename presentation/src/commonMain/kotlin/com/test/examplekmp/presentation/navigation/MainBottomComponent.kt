package com.test.examplekmp.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.test.examplekmp.presentation.ui.detail.DetailComponent
import com.test.examplekmp.presentation.ui.home.HomeComponent
import com.test.examplekmp.presentation.ui.map.MapComponent

interface MainBottomComponent {
    val childStackBottom: Value<ChildStack<*, ChildBottom>>
    fun openHome()
    fun openMap()
    fun openDetail(componentContext: ComponentContext, contentId: String, contentTypeId: String)

    sealed class ChildBottom {
        class HomeChild(val component: HomeComponent) : ChildBottom()
        class MapChild(val component: MapComponent) : ChildBottom()
        class DetailChild(val component: DetailComponent, val contentId: String,
            val contentTypeId: String): ChildBottom()
    }
}