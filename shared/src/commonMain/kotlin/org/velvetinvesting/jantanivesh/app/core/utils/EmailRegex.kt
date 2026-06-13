package org.velvetinvesting.jantanivesh.app.core.utils

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )
    return email.matches(emailRegex)
}