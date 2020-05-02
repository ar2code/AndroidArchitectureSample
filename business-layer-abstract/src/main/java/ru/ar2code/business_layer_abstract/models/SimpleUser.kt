package ru.ar2code.business_layer_abstract.models

/**
 * Это модель пользователя, с которой работает логика.
 * Я её специально ввел, чтобы показать маппинг из модели API
 */
data class SimpleUser(
    val isAdmin: Boolean,
    val name: String?
)