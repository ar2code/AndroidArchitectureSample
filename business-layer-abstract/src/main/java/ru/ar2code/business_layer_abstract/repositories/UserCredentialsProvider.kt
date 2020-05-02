package ru.ar2code.business_layer_abstract.repositories

import ru.ar2code.business_layer_abstract.models.LoginParameters

interface UserCredentialsProvider {

    fun save(loginParameters: LoginParameters?)

    fun fetch(): LoginParameters?
}