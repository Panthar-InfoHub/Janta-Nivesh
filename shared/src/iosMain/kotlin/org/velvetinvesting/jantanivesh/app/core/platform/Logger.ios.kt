package org.velvetinvesting.jantanivesh.app.core.platform

actual fun Log(tag: String, log: String) {
    println("$tag: $log")
}