package org.velvetinvesting.jantanivesh

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform