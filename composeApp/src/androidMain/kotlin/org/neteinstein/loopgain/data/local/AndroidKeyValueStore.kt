package org.neteinstein.loopgain.data.local

import android.content.Context

private class AndroidKeyValueStore(context: Context) : KeyValueStore {
    private val prefs = context.getSharedPreferences("loopgain_settings", Context.MODE_PRIVATE)

    override fun getString(key: String): String? = prefs.getString(key, null)

    override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}

actual fun platformKeyValueStore(): KeyValueStore = AndroidKeyValueStore(AndroidAppContext.instance)
