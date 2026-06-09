package org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.remote.model.PANVerifyDto
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.remote.model.TradingAccountPrefilledResponseDto
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.PANVerifyDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountPrefilledDomain

fun TradingAccountPrefilledResponseDto.toDomain(): TradingAccountPrefilledDomain {
    return TradingAccountPrefilledDomain(
        fullName = data.full_name,
        email = data.email,
        phoneNo = data.phone_no,
        dob = data.dob,
        gender = data.gender,
        panNo = data.pan_no,
        placeOfBirth = data.place_of_birth,
        fullAddress = data.full_address,
        uid = data.uid,
        pinCode = data.pin_code,
        city = data.city,
        district = data.district,
        state = data.state,
        country = data.country,
        martialStatus = data.martial_status,
        fatherName = data.father_name,
        motherName = data.mother_name
    )
}

fun PANVerifyDto.toDomain(): PANVerifyDomain {
    return PANVerifyDomain(
        status = data.pan_verified,
        message = message
    )
}
