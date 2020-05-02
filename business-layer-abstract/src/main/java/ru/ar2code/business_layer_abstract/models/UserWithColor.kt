package ru.ar2code.business_layer_abstract.models

data class UserWithColor(
    val isAuthorized : Boolean,
    val userName : String?,
    val color : Int?
)