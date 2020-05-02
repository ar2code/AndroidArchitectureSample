package ru.ar2code.business_layer.usecases

import ru.ar2code.business_layer_abstract.repositories.UserCredentialsProvider
import ru.ar2code.business_layer_abstract.usecases.AbstractLogoutUseCase

/**
 * Пример использования провайдера из вне.
 */
class LogoutUseCase(
    private val userCredentialsProvider: UserCredentialsProvider
) : AbstractLogoutUseCase() {

    override suspend fun run(params: Unit?): Boolean {
        userCredentialsProvider.save(null)
        return true
    }
}