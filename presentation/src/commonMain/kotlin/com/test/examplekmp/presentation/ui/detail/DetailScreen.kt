package com.test.examplekmp.presentation.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.essenty.backhandler.BackCallback
import com.seiko.imageloader.rememberImagePainter
import com.test.examplekmp.domain.entity.map.LatLng
import com.test.examplekmp.presentation.RR
import com.test.examplekmp.presentation.base.bindViewModel
import com.test.examplekmp.presentation.base.compose.component.HyperlinkText
import com.test.examplekmp.presentation.base.compose.theme.AppTheme
import com.test.examplekmp.presentation.base.compose.theme.Colors
import com.test.examplekmp.presentation.base.compose.theme.bold
import com.test.examplekmp.presentation.base.compose.theme.semiBold
import com.test.examplekmp.presentation.base.root.LocalComponentContext
import com.test.examplekmp.presentation.extension.extractUrl
import com.test.examplekmp.presentation.extension.getDetailText
import com.test.examplekmp.presentation.extension.getThumbnailUrl
import com.test.examplekmp.presentation.model.Marker
import com.test.examplekmp.presentation.ui.DefaultScreen
import com.test.examplekmp.presentation.ui.view.map.CameraPosition
import com.test.examplekmp.presentation.ui.view.map.CameraPositionState
import com.test.examplekmp.presentation.ui.view.map.NaverMap
import com.test.examplekmp.presentation.ui.view.map.PlaceMarker
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    component: DetailComponent,
    contentId: String,
    contentTypeId: String,
    viewModel: DetailViewModel = bindViewModel(parameters = {
        parametersOf(contentId, contentTypeId)
    })
) {
    val context = LocalComponentContext.current
    val backCallbackEnable = true

    DisposableEffect(backCallbackEnable) {
        val backCallback = BackCallback(backCallbackEnable) {
            component.onBackClick()
        }
        context.backHandler.register(backCallback)
        onDispose {
            viewModel.onCleared()
            context.backHandler.unregister(backCallback)
        }
    }

    val detailItem by viewModel.detailItem.collectAsState(initial = null)

    DefaultScreen(
        modifier = Modifier,
        backgroundColor = Colors.White,
        loading = viewModel.loading,
        oncreate = {
            launch {
                viewModel.getDetail(contentId, contentTypeId)
            }
        }
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                detailItem?.run {
                    DetailImage(firstImage = firstimage, onBackClick = {
                        component.onBackClick()
                    })

                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                        Text(
                            text = title,
                            style = AppTheme.typography.h3.bold(),
                            modifier = Modifier.padding(top = 10.dp, bottom = 30.dp),
                            color = Colors.Black,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        DetailInfo(
                            "전화번호", tel.getDetailText(), subContent = {},
                            lastItem = false
                        )
                        DetailInfo(
                            "홈페이지", homepage.getDetailText(), subContent = {},
                            useLink = true,
                            lastItem = false
                        )
                        DetailInfo(
                            "주소", addr1.getDetailText(), subContent = {
                                DetailMap(
                                    cameraPositionState = CameraPositionState(
                                        CameraPosition(
                                            LatLng(mapy.toDouble(), mapx.toDouble()),
                                            16f,
                                            0f,
                                            0f
                                        )
                                    ),
                                    selectMarker = viewModel.selectMarker.value
                                )
                            },
                            lastItem = false
                        )
                        DetailInfo(
                            "소개", overview.getDetailText(), subContent = {},
                            lastItem = true
                        )
                    }
            }
        }
    }
}


@Composable
fun BackIcon(buttonClick: () -> Unit) {
    Image(
        imageVector = Icons.Filled.ArrowBack,
        colorFilter = ColorFilter.tint(Colors.Purple_400),
        contentDescription = null,
        modifier = Modifier
            .padding(10.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(Colors.Gray_100_alpha)
            .padding(5.dp)
            .clickable { buttonClick() }
    )
}

@Composable
fun DetailInfo(title: String,
               content: String,
               lastItem: Boolean,
               useLink: Boolean = false,
               subContent: @Composable () -> Unit) {
    Text(
        text = title,
        color = Colors.Black,
        style = AppTheme.typography.body2.semiBold(),
    )

    val url = content.extractUrl()
    if(useLink && url.isNotEmpty()) {
        HyperlinkText(hyperlinkText = url, url = url)
    } else {
        Text(
            text = content,
            color = Colors.Gray_600,
            style = AppTheme.typography.body2,
        )
    }

    subContent()
    Spacer(modifier = Modifier.height(10.dp))

    if(!lastItem) {
        Divider(color = Colors.Gray_100, thickness = 1.dp)
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun DetailMap(cameraPositionState: CameraPositionState, selectMarker: Marker?) {
    Box(modifier = Modifier.fillMaxWidth().height(200.dp).padding(top = 10.dp)) {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            mapContent = {
                selectMarker?.run {
                    PlaceMarker(this,
                        onClick = {}
                    )
                }
            }
        )
    }
}

@Composable
fun DetailImage(firstImage: String, onBackClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        if (firstImage.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(firstImage.getThumbnailUrl()),
                contentDescription = "detail_image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop,
            )
        } else {
            Image(
                painter = dev.icerock.moko.resources.compose.painterResource(RR.images.ic_no_image),
                contentDescription = "empty_image",
                modifier = Modifier.align(Alignment.Center).size(200.dp),
                colorFilter = ColorFilter.tint(Colors.LightGray),
            )
        }
        BackIcon(
            buttonClick = {
                onBackClick.invoke()
            }
        )
    }
}