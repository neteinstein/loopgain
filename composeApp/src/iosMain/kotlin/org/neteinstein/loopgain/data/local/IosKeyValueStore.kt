package org.neteinstein.loopgain.data.local

import platform.Foundation.NSUserDefaults

private class IosKeyValueStore : KeyValueStore {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun getString(key: String): String? = defaults.stringForKey(key)

    override fun putString(key: String, value: String) {
        defaults.setObject(value, key)
    }
}

actual fun platformKeyValueStore(): KeyValueStore = IosKeyValueStore()
