package ru.ar2code.architecturesample.main

import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.ar2code.architecturesample.impl.UserCredentialsSharedProvider
import ru.ar2code.business_layer.usecases.LoginUseCase
import ru.ar2code.business_layer.usecases.CredsCheckValidUseCase
import ru.ar2code.business_layer_abstract.repositories.UserCredentialsProvider
import ru.ar2code.business_layer_abstract.repositories.UsersRepository
import ru.ar2code.business_layer_abstract.usecases.AbstractLoginUserCase
import ru.ar2code.business_layer_abstract.usecases.AbstractCredsCheckValidUseCase
import ru.ar2code.some_api.UserFakeRepository

val appModule = module {

    viewModel { MainViewModel(get(), get()) }

    factory { LoginUseCase(get(), get()) as AbstractLoginUserCase }
    factory { CredsCheckValidUseCase() as AbstractCredsCheckValidUseCase }
    factory { UserCredentialsSharedProvider(androidContext()) as UserCredentialsProvider }
    factory { UserFakeRepository() as UsersRepository }

}