package org.velvetinvesting.jantanivesh.app.features.profile.domain.usecase


import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.login.domain.repository.UserAuth

class MarkNotificationsAsReadUseCase(private val repository: UserAuth) {
    suspend operator fun invoke(): NetworkResponse<Unit, ErrorDomain> {
        return repository.markNotificationsAsRead()
    }
}
