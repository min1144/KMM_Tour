package com.test.examplekmp.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.test.examplekmp.presentation.RR
import com.test.examplekmp.presentation.base.bindViewModel
import com.test.examplekmp.presentation.base.compose.component.rememberScaffoldState
import com.test.examplekmp.presentation.base.compose.theme.Colors
import com.test.examplekmp.presentation.navigation.LocalNavigation
import com.test.examplekmp.presentation.navigation.goMain
import com.test.examplekmp.presentation.ui.DefaultScreen
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = bindViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val navigation = LocalNavigation.current

    DefaultScreen(
        scaffoldState = scaffoldState,
        backgroundColor = Colors.White,
        oncreate = {
            launch {
                viewModel.isNext.collectLatest {
                    if(!it) return@collectLatest
                    navigation.goMain()
                }
            }
        },
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(RR.images.splash_travel),
                contentDescription = "logo",
                modifier = Modifier.size(250.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Colors.Purple_600)
            )
        }
    }
}