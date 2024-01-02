import SwiftUI
import shared
import UIKit

struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea(edges: .vertical)
    }
}

 struct ContentView_Previews: PreviewProvider {
 	static var previews: some View {
 		ContentView()
 	}
 }
