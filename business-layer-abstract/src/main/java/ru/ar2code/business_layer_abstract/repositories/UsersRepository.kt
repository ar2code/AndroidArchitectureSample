package ru.ar2code.business_layer_abstract.repositories

import ru.ar2code.business_layer_abstract.models.SimpleUser

interface UsersRepository {
    suspend fun authorize(login: String?, password: String?): SimpleUser
}