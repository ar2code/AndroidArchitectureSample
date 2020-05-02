package ru.ar2code.some_api

import kotlinx.coroutines.delay
import ru.ar2code.business_layer_abstract.models.SimpleUser
import ru.ar2code.business_layer_abstract.repositories.UsersRepository

class UserFakeRepository : UsersRepository {

    private var users: List<User> = listOf(
        User(
            "Admin user",
            "admin",
            "admin",
            "fake address",
            System.currentTimeMillis(),
            Role.Admin
        ),
        User("Demo user", "demo", "demo", "fake address", System.currentTimeMillis(), Role.User)
    )

    override suspend fun authorize(login: String?, password: String?): SimpleUser {
        /**
         * Имитация запроса, вычислений и т.д.
         */
        delay(3000)

        val userByCreds = users.firstOrNull { it.login == login && it.password == password }

        /**
         * Мы возвращаем модель данных, которую понимает слой бизнес-логики.
         * API может выполнять любые операции, использовать любые свои данные, но по итогу возвращать данные, которые пониманет бизнес-логика.
         */
        return SimpleUser(
            isAdmin = userByCreds?.role == Role.Admin,
            name = userByCreds?.name
        )
    }

}