package com.test.examplekmp.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.test.examplekmp.presentation.RR
import com.test.examplekmp.presentation.base.compose.component.FullLoading
import com.test.examplekmp.presentation.base.compose.component.Scaffold
import com.test.examplekmp.presentation.base.compose.component.ScaffoldState
import com.test.examplekmp.presentation.base.compose.component.rememberScaffoldState
import com.test.examplekmp.presentation.base.compose.theme.AppTheme
import com.test.examplekmp.presentation.navigation.MainBottomComponent
import com.test.examplekmp.presentation.ui.detail.DetailScreen
import com.test.examplekmp.presentation.ui.home.HomeScreen
import com.test.examplekmp.presentation.ui.map.MapScreen
import com.test.examplekmp.presentation.util.log.Logger
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

private val logger = Logger.screen

@Composable
fun DefaultScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.background,
    oncreate: suspend CoroutineScope.() -> Unit = {},
    loading: Flow<Boolean> = emptyFlow(),
    content: @Composable (PaddingValues) -> Unit
) {
    Screen(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = topBar,
        backgroundColor = backgroundColor,
        statusBarColor = backgroundColor,
        onCreate = oncreate,
        content = content,
        loading = loading
    )
}

@Composable
private fun Screen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.background,
    statusBarColor: Color = backgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    onCreate: suspend CoroutineScope.() -> Unit = {},
    loading: Flow<Boolean> = emptyFlow(),
    content: @Composable (PaddingValues) -> Unit
) {
    LaunchedEffect(Unit) {
        logger.d("onLifeCycle : event : $onCreate")

        launch {
            onCreate()
        }
    }

    val showLoading = loading.collectAsState(initial = false)
    AppTheme {
        Scaffold(
                modifier = modifier,
                scaffoldState = scaffoldState,
                topBar = topBar,
                statusBarColor = statusBarColor,
                contentColor = contentColor,
            ) {
                content(it)
            }
            FullLoading(visible = showLoading.value)
        }
    }


data class ScreensBottom(val name: String, val openScreen: () -> Unit, val isSelected: Boolean)

@Composable
fun MainBottomNavigationScreen(
    component: MainBottomComponent,
    onCreate: suspend CoroutineScope.() -> Unit = {},
) {
    var selectedItem by remember{mutableIntStateOf(0)}
    val model by component.childStackBottom.subscribeAsState()

    val screens by remember {
        mutableStateOf(
            listOf(
                ScreensBottom("Home", component::openHome, false),
                ScreensBottom("Map", component::openMap, false),
            )
        )
    }

    LaunchedEffect(Unit) {
        launch {
            onCreate()
        }
    }

    AppTheme {
        androidx.compose.material3.Scaffold(
            bottomBar = {
                if(model.active.instance !is MainBottomComponent.ChildBottom.DetailChild) {
                    BottomAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        actions = {
                            screens.forEachIndexed { index, screensBottom ->
                                NavigationBarItem(
                                    icon = {
                                        when (screensBottom.name) {
                                            "Home" -> Icon(
                                                Icons.Outlined.Home,
                                                contentDescription = null
                                            )

                                            "Map" -> Icon(
                                                painter = painterResource(RR.images.ic_map),
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    label = { Text(text = screensBottom.name) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        screensBottom.openScreen()
                                    },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                    )
                }
            }
        ) {
            Children(
                stack = component.childStackBottom,
                animation = null,
            ) {
                when (val child = it.instance) {
                    is MainBottomComponent.ChildBottom.HomeChild -> {
                        HomeScreen(component = child.component)
                    }

                    is MainBottomComponent.ChildBottom.MapChild -> {
                        MapScreen(component = child.component)
                    }

                    is MainBottomComponent.ChildBottom.DetailChild -> {
                        DetailScreen(
                            component = child.component,
                            contentId = child.contentId,
                            contentTypeId = child.contentTypeId)
                    }
                }
            }
        }
    }
}