package org.velvetinvesting.jantanivesh.app.features.core.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.UserDataDomain

interface UserDataRepo {
    suspend fun getUserData(): NetworkResponse<UserDataDomain, ErrorDomain>
}