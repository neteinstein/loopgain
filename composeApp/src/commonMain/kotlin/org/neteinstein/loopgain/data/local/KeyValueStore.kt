package org.neteinstein.loopgain.data.local

/** A minimal, synchronous string key-value store — just enough to persist user preferences. */
interface KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
}

/** SharedPreferences on Android, NSUserDefaults on iOS. */
expect fun platformKeyValueStore(): KeyValueStore
