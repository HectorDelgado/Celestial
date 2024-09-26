import SwiftUI
import ComposeApp


@main
struct iOSApp: App {
    init() {
        KoinHelperKt.doInitKoin()
        //KoinHel
        //KoinHel
        //KoinHelperKt.initKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
