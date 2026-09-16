package org.neteinstein.loopgain

import android.app.Application
import org.neteinstein.loopgain.data.local.AndroidAppContext
import org.neteinstein.loopgain.di.initKoin

class LoopGainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidAppContext.instance = this
        initKoin()
    }
}
