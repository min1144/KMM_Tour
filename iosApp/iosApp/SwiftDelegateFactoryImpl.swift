import Foundation
import NMapsMap
import shared

class NMFMapViewDelegateWrapper: NSObject, NMFMapViewCameraDelegate {
    
    var target: PresentationSwiftNaverMapDelegate?

    var isMoving: Bool = false

    init(delegate: PresentationSwiftNaverMapDelegate? = nil) {
        self.target = delegate
    }
    
    func mapViewCameraIdle(_ mapView: NMFMapView) {
        target?.setOnCameraIdleListener(position: mapView.cameraPosition)
    }
    
    func mapView(_ mapView: NMFMapView, cameraWillChangeByReason reason: Int, animated: Bool) {
        target?.setOnCameraMoveStartedListener(reason: 1)
    }
    
    func mapView(_ mapView: NMFMapView, cameraIsChangingByReason reason: Int) {
        target?.setOnCameraMoveListener(position: mapView.cameraPosition)
    }
}

class SwiftDelegateFactoryImpl: PresentationSwiftDelegateFactory {
    
      func naverMapDelegate(delegate: PresentationSwiftNaverMapDelegate) -> NMFMapViewCameraDelegate {
            return NMFMapViewDelegateWrapper(delegate: delegate)
        }
}
