package org.velvetinvesting.jantanivesh.app.features.auth.domain.usecase

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.auth.domain.repository.MpinRepo

class UpdateMpinUseCase(
    private val repository: MpinRepo
) {
    suspend operator fun invoke(mpin: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.updateMpin(mpin)
    }
}
