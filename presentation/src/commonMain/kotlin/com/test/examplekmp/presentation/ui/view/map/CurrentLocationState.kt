package com.test.examplekmp.presentation.ui.view.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.test.examplekmp.domain.entity.map.LatLng
import com.test.examplekmp.presentation.model.Location

class CurrentLocationState(defaultLocation: LatLng, location: Location? = null) {

    private var _currentLocation = mutableStateOf(defaultLocation)

    private var _currentGPSLocation = mutableStateOf(location)

    val currentLocation
        get() = _currentLocation.value

    val currentGPSLocation
        get() = _currentGPSLocation.value

    fun updateGPSCurrentLocation(value: Location) {
        _currentGPSLocation.value = value
    }

    companion object {

        val Saver: Saver<CurrentLocationState, *> = listSaver(save = {
            listOf(it.currentLocation, it.currentGPSLocation)
        }, restore = {
            CurrentLocationState(it[0] as LatLng, it[1].run {
                if (this != null) {
                    this as Location
                } else {
                    null
                }
            })
        })
    }
}

@Composable
fun rememberCurrentLocationState(latLng: LatLng): CurrentLocationState {
    return rememberSaveable(saver = CurrentLocationState.Saver) {
        CurrentLocationState(latLng)
    }
}