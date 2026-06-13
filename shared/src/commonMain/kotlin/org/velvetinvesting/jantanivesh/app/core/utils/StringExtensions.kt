package org.velvetinvesting.jantanivesh.app.core.utils

fun String.filterDigits(): String {
    return this.filter { it.isDigit() }
}
