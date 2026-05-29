package org.velvetinvesting.jantanivesh.app.core.platform

import platform.Foundation.NSUserDefaults
import kotlin.collections.forEach

class IosSharedPreferences : SharedPreference {

    private val prefs = NSUserDefaults.standardUserDefaults

    override fun setString(key: String, value: String) {
        prefs.setObject(value, forKey = key)
    }

    override fun getString(key: String): String? = prefs.stringForKey(key)

    override fun setBoolean(key: String, value: Boolean) {
        prefs.setBool(value, forKey = key)
    }

    override fun getBoolean(key: String): Boolean? =
        if (prefs.objectForKey(key) != null) prefs.boolForKey(key) else null

    override fun setInt(key: String, value: Int) {
        prefs.setInteger(value.toLong(), forKey = key) // NSInteger is Long
    }

    override fun getInt(key: String): Int? =
        if (prefs.objectForKey(key) != null) prefs.integerForKey(key).toInt() else null

    override fun remove(key: String) {
        prefs.removeObjectForKey(key)
    }

    override fun clear() {
        prefs.dictionaryRepresentation().keys.forEach {
            prefs.removeObjectForKey(it as String)
        }
    }
}