package org.neteinstein.loopgain

import android.app.Application
import org.neteinstein.loopgain.di.initKoin

class LoopGainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
