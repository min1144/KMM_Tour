package com.test.examplekmp.presentation.base.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
data class ImmutableModel<T>(val data : T)

@Stable
data class StableModel<T>(val data: T)