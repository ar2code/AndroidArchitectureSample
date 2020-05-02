package ru.ar2code.architecturesample.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.ar2code.business_layer_abstract.models.UserWithColor
import ru.ar2code.business_layer_abstract.usecases.AbstractLoginUserCase
import ru.ar2code.business_layer_abstract.usecases.AbstractCredsCheckValidUseCase
import ru.ar2code.common.ObservableUseCaseCommand
import ru.ar2code.mutableliveevent.EventArgs
import ru.ar2code.mutableliveevent.MutableLiveEvent

class MainViewModel(
    loginUseCase: AbstractLoginUserCase,
    credsValidityUseCase: AbstractCredsCheckValidUseCase
) : ViewModel() {

    private var login: String? = null
    private var password: String? = null

    private val authorizationSuccessMutableLive = MutableLiveEvent<EventArgs<String>>()
    val authorizationSuccessLive = authorizationSuccessMutableLive as LiveData<EventArgs<String>>

    private val authorizationErrorMutableLive = MutableLiveEvent<EventArgs<Unit>>()
    val authorizationErrorLive = authorizationErrorMutableLive as LiveData<EventArgs<Unit>>

    private val refreshLoginViewMutableLive = MutableLiveEvent<EventArgs<Unit>>()
    val refreshLoginViewLive = refreshLoginViewMutableLive as LiveData<EventArgs<Unit>>

    val loginCommand = ObservableUseCaseCommand.createViewModelCommand(
        this,
        loginUseCase,
        successListener = {
            onAuthorized(it)
        }
    )

    val loginValidityCommand = ObservableUseCaseCommand.createViewModelCommand(
        this,
        credsValidityUseCase,
        successListener = {
            onLoginValidity(it)
        }
    )

    val passwordValidityCommand = ObservableUseCaseCommand.createViewModelCommand(
        this,
        credsValidityUseCase,
        successListener = {
            onPasswordValidity(it)
        }
    )

    init {
        loginCommand.canExecute = false
    }

    private fun onLoginValidity(result: String?) {
        login = result
        updateCanExecuteLogin()
        refreshView()
    }

    private fun onPasswordValidity(result: String?) {
        password = result
        updateCanExecuteLogin()
        refreshView()
    }

    private fun onAuthorized(userWithColor: UserWithColor?) {
        val isAuthorized = userWithColor?.isAuthorized == true
        if (isAuthorized) {
            authorizationSuccessMutableLive.postValue(EventArgs(userWithColor?.userName))
        } else {
            authorizationErrorMutableLive.postValue(EventArgs(Unit))
        }
    }

    private fun updateCanExecuteLogin() {
        val isValid = !login.isNullOrEmpty() && !password.isNullOrEmpty()
        loginCommand.canExecute = isValid
    }

    private fun refreshView() {
        refreshLoginViewMutableLive.postValue(EventArgs(Unit))
    }
}