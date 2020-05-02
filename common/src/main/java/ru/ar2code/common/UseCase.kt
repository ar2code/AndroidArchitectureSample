package ru.ar2code.common

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class UseCase<Type, Params>() where Type : Any {

    private var job: Job? = null

    protected var command: ObservableUseCaseCommand<Type, Params>? = null

    protected abstract suspend fun run(params: Params?): Type

    open fun execute(command: ObservableUseCaseCommand<Type, Params>, params: Params?) {
        this.command = command
        executeCommandCoroutine(command, params)
    }

    open fun cancel() {
        job?.cancel()
        command?.emitCanceled()
        clearCommand()
    }

    private fun onExecutionFinished(result: Type) {
        command?.emitSuccessResult(result)
    }

    private fun executeCommandCoroutine(
        command: ObservableUseCaseCommand<Type, Params>,
        params: Params?
    ) {
        job = command.scope.launch(command.context) {
            val result = executeRunWithErrorHandling(params)
            if (result != null) {
                onExecutionFinished(result)
            }
            clearCommand()
        }
    }

    private fun clearCommand() {
        command = null
    }

    private suspend fun executeRunWithErrorHandling(params: Params?): Type? {
        try {
            return run(params)
        } catch (e: Exception) {
            emitError(e)
        }
        return null
    }

    private fun emitError(exception: Exception) {
        command?.let {
            command?.emitError(exception)
        } ?: kotlin.run {
            throw exception
        }
    }
}