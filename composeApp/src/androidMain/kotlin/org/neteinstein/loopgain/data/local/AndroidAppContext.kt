package org.neteinstein.loopgain.data.local

import android.content.Context

/**
 * Set once from `LoopGainApplication.onCreate()`, before Koin starts, so [platformKeyValueStore]
 * can reach a [Context] without threading Android types through commonMain/Koin's shared module.
 */
object AndroidAppContext {
    lateinit var instance: Context
}
