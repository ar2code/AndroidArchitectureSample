package ru.ar2code.business_layer_abstract.usecases

import ru.ar2code.business_layer_abstract.models.LoginParameters
import ru.ar2code.business_layer_abstract.models.UserWithColor
import ru.ar2code.common.UseCase

abstract class AbstractLoginUserCase : UseCase<UserWithColor, LoginParameters>()