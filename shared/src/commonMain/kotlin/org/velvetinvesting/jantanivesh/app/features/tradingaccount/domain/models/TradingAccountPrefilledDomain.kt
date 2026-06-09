package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models

data class TradingAccountPrefilledDomain(
    val fullName: String,
    val email: String,
    val phoneNo: String,
    val dob: String,
    val gender: String,
    val panNo: String,
    val placeOfBirth: String,
    val fullAddress: String,
    val uid: String,
    val pinCode: String,
    val city: String,
    val district: String,
    val state: String,
    val country: String,
    val martialStatus: String,
    val fatherName: String,
    val motherName: String
)