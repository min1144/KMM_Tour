import SwiftUI
import shared

@main
struct iOSApp: App {

    init() {
        KoinApplication.start()
        Platform_iosKt.doInitApp()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
