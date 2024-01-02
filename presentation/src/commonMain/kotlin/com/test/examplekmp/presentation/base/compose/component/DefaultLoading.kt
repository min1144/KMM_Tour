package com.test.examplekmp.presentation.base.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.test.examplekmp.presentation.base.compose.theme.Colors

@Composable
fun FullLoading(
    visible: Boolean
) {
    if (visible) {
       Box (
           modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center
       )  {
           CircularProgressIndicator(color = Colors.Purple_300)
       }
    }
}