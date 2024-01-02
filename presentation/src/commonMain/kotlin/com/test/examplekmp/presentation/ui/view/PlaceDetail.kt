package com.test.examplekmp.presentation.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.test.examplekmp.domain.entity.base.ContentType
import com.test.examplekmp.presentation.base.compose.theme.AppTheme
import com.test.examplekmp.presentation.base.compose.theme.Colors
import com.test.examplekmp.presentation.base.compose.theme.bold
import com.test.examplekmp.presentation.model.Marker

@Composable
fun PlaceDetail(
    marker: Marker,
    onClick: (item: Marker) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(30.dp)
    ) {
        PlaceDetailCardItem(marker = marker,
            cardClick = {
                onClick(it)
            })
    }
}

@Composable
fun BoxScope.PlaceDetailCardItem(marker: Marker, cardClick: (marker: Marker) -> Unit) {
    Card(
        modifier = Modifier.height(80.dp)
            .fillMaxWidth()
            .align(Alignment.BottomStart)
            .clickable {
                cardClick(marker)
            },
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(
            containerColor = Colors.White,
        ),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(url = marker.thumbnailUrl),
                contentDescription = "detailImage",
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier.fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = marker.name,
                    modifier = Modifier.fillMaxWidth(),
                    style = AppTheme.typography.body2.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = ContentType.fromIdToText(marker.contentTypeId.toInt()),
                    modifier = Modifier.fillMaxWidth(),
                    style = AppTheme.typography.caption1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}