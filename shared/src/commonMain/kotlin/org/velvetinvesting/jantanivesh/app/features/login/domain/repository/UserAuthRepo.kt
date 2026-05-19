package org.velvetinvesting.jantanivesh.app.features.login.domain.repository

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import org.koin.dsl.module

interface UserAuthRepo {
    suspend fun sendOtp(number:String) : UserOtpDomain
    suspend fun verifyOtp(otp:String) : Boolean
}

data class UserOtpDto(
    val fund:String= "SIP",
)


enum class Fund {
    SIP,LUMPSUM
}

data class UserOtpDomain(
    val otp: Fund
)

fun UserOtpDto.toDomain(): UserOtpDomain {
    return UserOtpDomain(
        otp = if (fund.capitalize(Locale.current) == Fund.SIP.name) Fund.SIP else Fund.LUMPSUM
    )
}