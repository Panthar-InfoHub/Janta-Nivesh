package org.velvetinvesting.jantanivesh.app.features.login.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.login.domain.repository.UserAuth

class LoginWithNumberUseCase(private val repository: UserAuth) {
    suspend operator fun invoke(number: String) = repository.loginWithNumber(number)
}
