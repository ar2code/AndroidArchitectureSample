package ru.ar2code.some_api

/**
 * Это модель данных пользователя в API. Она используется для работы репозитория,
 * при передачи в бизнес-логику маппится в модель, понятную слою бизнес-логики
 */
data class User(
    val name : String,
    val login : String,
    val password : String,
    val address : String?,
    val lastEnter : Long?,
    val role: Role
)

enum class Role { Admin, User }