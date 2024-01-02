import shared
import Foundation

typealias KoinApplication = Koin_coreKoinApplication
typealias Koin = Koin_coreKoin

extension KoinApplication {
    static let shared = companion.start(swiftDelegateFactory: SwiftDelegateFactoryImpl())
    
    @discardableResult
    static func start() -> KoinApplication {
        shared
    }
}
