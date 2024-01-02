package com.test.examplekmp.presentation.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.test.examplekmp.domain.entity.Event
import com.test.examplekmp.domain.entity.HomeItem
import com.test.examplekmp.domain.entity.HomeItemViewType
import com.test.examplekmp.domain.entity.Hotel
import com.test.examplekmp.domain.entity.Keyword
import com.test.examplekmp.presentation.RR
import com.test.examplekmp.presentation.base.bindViewModel
import com.test.examplekmp.presentation.base.compose.component.UnderlineText
import com.test.examplekmp.presentation.base.compose.component.rememberScaffoldState
import com.test.examplekmp.presentation.base.compose.rememberThumbnailUrl
import com.test.examplekmp.presentation.base.compose.theme.AppTheme
import com.test.examplekmp.presentation.base.compose.theme.Colors
import com.test.examplekmp.presentation.base.compose.theme.bold
import com.test.examplekmp.presentation.base.compose.theme.createH2TextStyle
import com.test.examplekmp.presentation.extension.getThumbnailUrl
import com.test.examplekmp.presentation.ui.DefaultScreen
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun HomeScreen(
    component: HomeComponent,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = bindViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()
    val keywordList by viewModel.keywordList.collectAsState()

    DefaultScreen(
        modifier = modifier.padding(bottom = 80.dp),
        backgroundColor = Colors.White,
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar()
        },
        loading = viewModel.loading,
    ) {
        val paddingModifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState
        ) {
            items(keywordList, key = {it.hashCode()}) { homeItem ->
                when (homeItem.viewType) {
                    HomeItemViewType.HEADER -> {
                        TitleHeader(
                            title = homeItem.homeHeader?.title.toString(),
                        )
                    }
                    HomeItemViewType.PAGER -> {
                        HorizontalPager(
                            component = component,
                            homeItem = homeItem,
                            paddingModifier = paddingModifier
                        )
                    }
                    HomeItemViewType.LIST -> {
                        ListItem(
                            homeItem = homeItem,
                            component = component,
                            paddingModifier = paddingModifier
                        )
                    }
                    HomeItemViewType.GRID -> {
                        Grid(
                            homeItem = homeItem,
                            component = component
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()
        .height(60.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Travel.",
            fontFamily = fontFamilyResource(RR.fonts.pilseung.pilseung),
            style = createH2TextStyle(),
            color = Colors.Purple_400
        )
    }
}

@Composable
fun TitleHeader(title: String) {
        UnderlineText(
            title = title,
            modifier = Modifier.wrapContentSize().padding(
                top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp
            )
        )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPager(
    component: HomeComponent,
    homeItem: HomeItem,
    paddingModifier: Modifier
) {
    val pagerState = rememberPagerState(pageCount = {
        homeItem.keyword?.size ?: 0
    })
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.height(210.dp),
        contentPadding = PaddingValues(start = 30.dp, end = 30.dp),
        pageSize = PageSize.Fixed(200.dp),
    ) {
        if(homeItem.keyword?.get(it) != null) {
            CardItem(
                paddingModifier = paddingModifier,
                keyword = homeItem.keyword!![it],
                component = component)
        }
    }
}

@Composable
fun ListItem(
    paddingModifier: Modifier,
    component: HomeComponent,
    homeItem: HomeItem,
) {
    val size = remember { mutableStateOf(homeItem.event?.size ?: 0) }
    repeat(size.value) {
        homeItem.event?.get(it)?.run {
            EventItem(paddingModifier, component, this)

            val lastIndex by derivedStateOf { it < 3 }
            val height = if(lastIndex) 10.dp else 0.dp
            Spacer(modifier = Modifier.height(height))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Grid(homeItem: HomeItem, component: HomeComponent) {
    val size = remember { mutableStateOf(homeItem.hotel?.size ?: 0) }
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
        maxItemsInEachRow = 2,
    ) {
        repeat(size.value) {
            val hotel = homeItem.hotel?.get(it) ?: return@repeat
            GridItem(hotel = hotel, component = component)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowScope.GridItem(hotel: Hotel, component: HomeComponent) {
    Column(
        modifier = Modifier
            .size(50.dp, 170.dp)
            .padding(horizontal = 5.dp)
            .weight(1f, true)
            .clickable {
                component.onItemClick(contentId = hotel.contentid.toString(),
                    contentTypeId = hotel.contenttypeid.toString()
                )
            }
    ) {
        PlaceImage(thumbnailUrl = hotel.firstimage,
            modifier = Modifier.height(120.dp).padding(bottom = 10.dp))

        Text(
            text = hotel.title.toString(),
            modifier = Modifier.fillMaxWidth(),
            style = AppTheme.typography.body2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = hotel.regionText,
            modifier = Modifier.fillMaxWidth(),
            style = AppTheme.typography.caption1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CardItem(paddingModifier: Modifier,
             keyword: Keyword,
             component: HomeComponent) {
    Card(
        modifier = Modifier.fillMaxHeight().widthIn(max = 190.dp)
            .clickable {
                component.onItemClick(contentId = keyword.contentid.toString(),
                    contentTypeId = keyword.contenttypeid.toString()
                )
            },
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(
            containerColor = Colors.White,
        ),
        elevation = CardDefaults.cardElevation(1.dp)) {
            CardChild(keyword = keyword,
                paddingModifier = paddingModifier)
    }
}

@Composable
fun CardChild(keyword: Keyword, paddingModifier: Modifier) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top) {
        PlaceImage(
            thumbnailUrl = keyword.firstimage,
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )

        Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())

        Text(
            text = keyword.title.toString(),
            modifier = paddingModifier,
            style = AppTheme.typography.body2.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = keyword.addr1.toString(),
            modifier = paddingModifier,
            style = AppTheme.typography.caption1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.weight(1f, true))
        BulletList(listOf(keyword.areaText, keyword.contentTypeText))
    }
}

@Composable
fun BulletList(bulletList: List<String>) {
    val paddingModifier = Modifier
                .drawBehind {
                    drawRoundRect(
                        Color(Colors.Purple_100_alpha.toArgb()),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )
                }.padding(horizontal = 5.dp, vertical = 2.dp)

    Row(modifier = Modifier.fillMaxWidth().height(30.dp).padding(start = 10.dp, bottom = 10.dp)) {
        bulletList.forEach {
            BulletItem(
                title = it,
                modifier = paddingModifier
            )

            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun BulletItem(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(title,
        modifier = modifier,
        style = AppTheme.typography.caption2
    )
}

@Composable
fun EventItem(paddingModifier: Modifier,
              component: HomeComponent,
              event: Event) {
    val rowHeight = 60.dp
    Row(modifier = Modifier.height(rowHeight)
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .background(Colors.White)
        .clickable {
            component.onItemClick(contentId = event.contentid.toString(),
                contentTypeId = event.contenttypeid.toString()
            )
        }
    ) {
        PlaceImage(thumbnailUrl = event.firstimage,
            modifier = Modifier.size(rowHeight))

        Text(text = event.title.toString(),
            modifier = paddingModifier.align(Alignment.CenterVertically),
            style = AppTheme.typography.body2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PlaceImage(thumbnailUrl: String?, modifier: Modifier) {
    if(!thumbnailUrl.isNullOrEmpty()) {
        Image(
            painter =  rememberImagePainter(rememberThumbnailUrl(thumbnailUrl.getThumbnailUrl())),
            contentDescription = "image",
            modifier = modifier,
            colorFilter = null,
            contentScale = ContentScale.Crop,
        )
    } else {
        Box(modifier = Modifier,
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = dev.icerock.moko.resources.compose.painterResource(RR.images.ic_no_image),
                contentDescription = "empty_image",
                modifier = modifier.size(50.dp),
                colorFilter = ColorFilter.tint(Colors.LightGray),
            )
        }
    }
}