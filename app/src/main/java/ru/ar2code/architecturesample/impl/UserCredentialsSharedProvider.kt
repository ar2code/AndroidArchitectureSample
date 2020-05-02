package ru.ar2code.architecturesample.impl

import android.content.Context
import ru.ar2code.business_layer_abstract.models.LoginParameters
import ru.ar2code.business_layer_abstract.repositories.UserCredentialsProvider

/**
 * Это пример, как можно передать в сценарий провайдер, который взаимодействует с андроид.
 * Здесь это sharedPreferences.
 */
class UserCredentialsSharedProvider(context: Context) : UserCredentialsProvider {

    companion object {
        private const val USER_CACHE_PREFS = "user_login_cache_prefs"
        private const val USER_LOGIN_KEY = "login"
        private const val USER_PASSWORD_KEY = "password"
    }

    private val sharedPreferences =
        context.getSharedPreferences(USER_CACHE_PREFS, Context.MODE_PRIVATE)

    override fun save(loginParameters: LoginParameters?) {
        sharedPreferences.edit()
            .putString(USER_LOGIN_KEY, loginParameters?.login)
            .putString(USER_PASSWORD_KEY, loginParameters?.password)
            .apply()
    }

    override fun fetch(): LoginParameters? {
        return LoginParameters(
            sharedPreferences.getString(USER_LOGIN_KEY, "").orEmpty(),
            sharedPreferences.getString(USER_PASSWORD_KEY, "").orEmpty()
        )
    }
}