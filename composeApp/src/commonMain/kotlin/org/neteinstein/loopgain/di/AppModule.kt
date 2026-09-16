package org.neteinstein.loopgain.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.neteinstein.loopgain.data.repository.CardRepository
import org.neteinstein.loopgain.data.repository.DefaultCardRepository
import org.neteinstein.loopgain.data.repository.InMemorySessionHistoryRepository
import org.neteinstein.loopgain.data.repository.SessionHistoryRepository
import org.neteinstein.loopgain.domain.model.SessionConfig
import org.neteinstein.loopgain.ui.viewmodel.SessionViewModel

val appModule = module {
    single { DefaultCardRepository() } bind CardRepository::class
    single { InMemorySessionHistoryRepository() } bind SessionHistoryRepository::class
    single { SessionConfig() }
    viewModel { SessionViewModel(get(), get(), get()) }
}

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
