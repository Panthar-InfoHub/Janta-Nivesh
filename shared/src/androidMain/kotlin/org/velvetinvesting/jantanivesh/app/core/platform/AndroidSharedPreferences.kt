package org.velvetinvesting.jantanivesh.app.core.platform

import android.content.Context
import androidx.core.content.edit

class AndroidSharedPreferences(
    private val context: Context,
) : SharedPreference {

    val prefs = context.getSharedPreferences("evolve_prefs", Context.MODE_PRIVATE)!!

    override fun setString(key: String, value: String) = prefs.edit { putString(key, value) }
    override fun getString(key: String): String? = prefs.getString(key, null)
    override fun setBoolean(key: String, value: Boolean) = prefs.edit { putBoolean(key, value) }
    override fun getBoolean(key: String): Boolean? = if (prefs.contains(key)) prefs.getBoolean(key, false) else null
    override fun setInt(key: String, value: Int) = prefs.edit { putInt(key, value) }
    override fun getInt(key: String): Int? = if (prefs.contains(key)) prefs.getInt(key, 0) else null
    override fun remove(key: String) = prefs.edit { remove(key) }
    override fun clear() = prefs.edit { clear() }
}