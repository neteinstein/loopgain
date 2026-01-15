import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        AppModuleKt.initKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
