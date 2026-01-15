package org.neteinstein.loopgain.di

import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    // Add your dependencies here
}

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
