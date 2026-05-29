package org.velvetinvesting.jantanivesh.app.core.platform

import android.util.Log

actual fun Log(tag: String, log: String) {
    Log.d(tag,log)
}