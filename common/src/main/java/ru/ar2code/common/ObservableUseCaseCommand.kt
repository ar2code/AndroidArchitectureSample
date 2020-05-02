package ru.ar2code.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ar2code.mutableliveevent.EventArgs
import ru.ar2code.mutableliveevent.MutableLiveEvent
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

class ObservableUseCaseCommand<Type, Params>(
    internal val scope: CoroutineScope,
    internal val context: CoroutineContext,
    private val attachedUseCase: UseCase<Type, Params>
) where Type : Any {

    companion object {
        fun <Type, Params> createViewModelCommand(
            viewModel: ViewModel,
            useCase: UseCase<Type, Params>,
            successListener: ((result: Type?) -> Unit)? = null,
            errorListener: ((Exception) -> Unit)? = null
        ): ObservableUseCaseCommand<Type, Params> where Type : Any {
            return ObservableUseCaseCommand(
                viewModel.viewModelScope,
                Dispatchers.Default,
                useCase
            ).also {
                it.onGeneralErrorListener = errorListener
                it.onSuccessListener = successListener
            }
        }
    }

    var isExecuting: Boolean = false
        set(value) {
            field = value
            isExecutingLiveInternal.value = value
        }

    var canExecute: Boolean = false
        set(value) {
            field = value
            canExecuteLiveInternal.value = value
        }

    private val isExecutingLiveInternal = MutableLiveData<Boolean>()
    val isExecutingLive = isExecutingLiveInternal as LiveData<Boolean>

    private val canExecuteLiveInternal = MutableLiveData<Boolean>()
    val canExecuteLive = canExecuteLiveInternal as LiveData<Boolean>

    private val resultInternal = MutableLiveEvent<EventArgs<Type>>()
    val result = resultInternal as LiveData<EventArgs<Type>>

    private val errorInternal = MutableLiveEvent<EventArgs<Exception>>()
    val error = errorInternal as LiveData<EventArgs<Exception>>

    private var onSuccessListener: ((Type?) -> Unit)? = null
    private var onGeneralErrorListener: ((Exception) -> Unit)? = null

    init {
        isExecuting = false
        canExecute = true
    }

    fun execute(params: Params? = null) {
        if (!canExecute || isExecuting)
            return

        isExecuting = true

        run(params)
    }

    fun cancel() {
        cancelRun()
        isExecuting = false
    }

    private fun run(params: Params?) {
        attachedUseCase.execute(this, params)
    }

    private fun cancelRun() {
        attachedUseCase.cancel()
    }

    private fun onSuccess(result: Type?) {
        onSuccessListener?.invoke(result)
        resultInternal.value = EventArgs(result)
        isExecuting = false
    }

    private fun onError(failure: Exception) {
        onGeneralErrorListener?.invoke(failure)
        errorInternal.value = EventArgs(failure)
        isExecuting = false
    }

    private fun onCancel() {
        isExecuting = false
    }

    internal fun emitSuccessResult(result: Type?) {
        scope.launch(Dispatchers.Main) {
            onSuccess(result)
        }
    }

    internal fun emitError(failure: Exception) {
        scope.launch(Dispatchers.Main) {
            onError(failure)
        }
    }

    internal fun emitCanceled() {
        scope.launch(Dispatchers.Main) {
            onCancel()
        }
    }
}