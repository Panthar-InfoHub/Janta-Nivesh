package org.velvetinvesting.jantanivesh.app.features.core.domain.usecase

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.UserDataDomain
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.UserDataRepo

class GetUserDataUseCase(
    private val repository: UserDataRepo
) {
    suspend operator fun invoke(): NetworkResponse<UserDataDomain, ErrorDomain> {
        return repository.getUserData()
    }
}
