package org.neteinstein.loopgain.di

import org.koin.core.context.startKoin

/**
 * Starts Koin with [appModule]. Called from `LoopGainApplication.onCreate()` on Android and from
 * `iosApp/iosApp/iOSApp.swift`'s `init()` (as `InitKoinKt.initKoin()`) on iOS — see AGENTS.md.
 *
 * Kept in its own file, apart from [appModule], so this is the only top-level declaration
 * Kotlin/Native's Objective-C export has to consider for the generated `InitKoinKt` header
 * class — see the failing `ios-build` CI check's history for why that isolation was worth doing.
 */
fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
