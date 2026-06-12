package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models

enum class SIPStatus {
    SUCCESS,PENDING,REQUESTED;

    companion object {
        fun getStatus(value: String): SIPStatus?{
            when(value){
                "SUCCESS" -> return SUCCESS
                "PENDING" -> return PENDING
                "REQUESTED" -> return REQUESTED
                else -> return null
            }
        }
    }
}