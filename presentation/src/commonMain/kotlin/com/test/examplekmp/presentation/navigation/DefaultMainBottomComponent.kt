package com.test.examplekmp.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.test.examplekmp.presentation.ui.detail.DefaultDetailComponent
import com.test.examplekmp.presentation.ui.detail.DetailComponent
import com.test.examplekmp.presentation.ui.home.DefaultHomeComponent
import com.test.examplekmp.presentation.ui.home.HomeComponent
import com.test.examplekmp.presentation.ui.map.DefaultMapComponent
import com.test.examplekmp.presentation.ui.map.MapComponent

class DefaultMainBottomComponent(
    componentContext: ComponentContext
) : MainBottomComponent, ComponentContext by componentContext {
    private val navigationBottomStackNavigation = StackNavigation<ConfigBottom>()

    private val _childStackBottom =
        childStack(
            source = navigationBottomStackNavigation,
            initialConfiguration = ConfigBottom.Home,
            handleBackButton = true,
            childFactory = ::createChildBottom,
            key = "mainStack"
        )

    override val childStackBottom: Value<ChildStack<*, MainBottomComponent.ChildBottom>> =
        _childStackBottom

    private fun mapComponent(componentContext: ComponentContext): MapComponent =
        DefaultMapComponent(
            componentContext = componentContext,
            onItemClicked = { contentId, contentTypeId ->
                openDetail(componentContext, contentId, contentTypeId)
            }
        )

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        DefaultHomeComponent(
            componentContext = componentContext,
            onItemClicked = { contentId, contentTypeId ->
                openDetail(componentContext, contentId, contentTypeId)
            }
        )

    private fun detailComponent(): DetailComponent =
        DefaultDetailComponent(
            onBackClick = {
                navigationBottomStackNavigation.pop()
            },
        )

    private fun createChildBottom(
        config: ConfigBottom,
        componentContext: ComponentContext
    ): MainBottomComponent.ChildBottom =
        when (config) {
            is ConfigBottom.Home -> MainBottomComponent.ChildBottom.HomeChild(homeComponent(componentContext))

            is ConfigBottom.Map -> MainBottomComponent.ChildBottom.MapChild(mapComponent((componentContext)))

            is ConfigBottom.Detail -> MainBottomComponent.ChildBottom.DetailChild(detailComponent(),
                contentId = config.contentId, contentTypeId = config.contentTypeId)
        }

    override fun openHome() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Home)
    }

    override fun openMap() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Map)
    }

    override fun openDetail(componentContext: ComponentContext, contentId: String, contentTypeId: String) {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Detail(contentId, contentTypeId))
    }

    sealed class ConfigBottom() : Parcelable {
        @Parcelize
        data object Home : ConfigBottom()

        @Parcelize
        data object Map : ConfigBottom()

        @Parcelize
        data class Detail(val contentId: String, val contentTypeId: String): ConfigBottom()
    }
}