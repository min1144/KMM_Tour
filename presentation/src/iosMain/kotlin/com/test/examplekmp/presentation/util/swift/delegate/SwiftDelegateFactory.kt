package com.test.examplekmp.presentation.util.swift.delegate

import cocoapods.NMapsMap.NMFCameraPosition
import cocoapods.NMapsMap.NMFMapViewCameraDelegateProtocol


interface SwiftDelegateFactory {
    fun naverMapDelegate(delegate: SwiftNaverMapDelegate): NMFMapViewCameraDelegateProtocol
}

interface SwiftNaverMapDelegate {

    fun setOnCameraIdleListener(position: NMFCameraPosition)

    fun setOnCameraMoveCanceledListener(position: NMFCameraPosition)

    fun setOnCameraMoveStartedListener(reason: Int)

    fun setOnCameraMoveListener(position: NMFCameraPosition)
}