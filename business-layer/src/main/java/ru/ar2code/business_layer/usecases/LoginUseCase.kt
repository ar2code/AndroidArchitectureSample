package ru.ar2code.business_layer.usecases

import android.graphics.Color
import ru.ar2code.business_layer_abstract.models.LoginParameters
import ru.ar2code.business_layer_abstract.models.UserWithColor
import ru.ar2code.business_layer_abstract.repositories.UserCredentialsProvider
import ru.ar2code.business_layer_abstract.repositories.UsersRepository
import ru.ar2code.business_layer_abstract.usecases.AbstractLoginUserCase

class LoginUseCase(
    private val usersRepository: UsersRepository,
    private val userCredentialsProvider: UserCredentialsProvider
) : AbstractLoginUserCase() {

    override suspend fun run(params: LoginParameters?): UserWithColor {

        val authorizationResult = usersRepository.authorize(params?.login, params?.password)

        /**
         * Некоторая логика с нашим пользователем.
         * Мы можем создать сущности из домена, если сущности обладают своей внутренней логикой
         * и/или поместить логику сюда, в сценарий
         *
         * Например, проверяем авторизовался ли пользователь.
         * Считаем, что если репозиторий вернул имя пользователя, значит авторизовался
         *
         * А если авторизовался, то сохраняем данные авторизации.
         * Сохраненные данные по сути ничего не делают, userCredentialsProvider просто показывает,
         * как можно использовать android shared preferences внутри нашей логики, которая ничего не знает об этом.
         */

        val isAuthorized = !authorizationResult.name.isNullOrEmpty()

        if (isAuthorized) {
            userCredentialsProvider.save(params)
        }

        /**
         * Мы возвращаем уже готовую для представления модель
         */
        return UserWithColor(
            isAuthorized,
            authorizationResult.name,
            getColorForUserName(authorizationResult.name)
        )
    }

    private fun getColorForUserName(name: String?): Int? {
        return when (name) {
            "Admin user" -> Color.BLUE
            "Demo user" -> Color.YELLOW
            else -> Color.RED
        }
    }
}