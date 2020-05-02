package ru.ar2code.common

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner

fun <Type, Params> View.bindClickToCommand(
    command: ObservableUseCaseCommand<Type, Params>,
    params: Params? = null
) where Type : Any {
    this.setOnClickListener { command.execute(params) }
}
/**
 * Full bind view with command
 * Invoke [Command.execute] on view click
 * Also view observes [Command.isExecuting] and [Command.canExecute].
 * View will be disabled while command is executing or can not be executed.
 */
fun <Type, Params> View.bindCommand(
    lifecycleOwner: LifecycleOwner,
    command: ObservableUseCaseCommand<Type, Params>,
    params: (() -> Params?)? = null
) where Type : Any {
    this.setOnClickListener { command.execute(params?.invoke()) }
    bindEnableWithCommandExecuting(lifecycleOwner, command)
    bindEnableWithCommandCanExecute(lifecycleOwner, command)
}

fun <Type, Params> View.bindEnableWithCommandExecuting(
    lifecycleOwner: LifecycleOwner,
    command: ObservableUseCaseCommand<Type, Params>
) where Type : Any {
    command.isExecutingLive.observe(
        lifecycleOwner,
        androidx.lifecycle.Observer { isExecuting ->
            val canExecute = command.canExecuteLive.value ?: false
            this.isEnabled = canExecute && isExecuting == false
        })
}

fun <Type, Params> View.bindEnableWithCommandCanExecute(
    lifecycleOwner: LifecycleOwner,
    command: ObservableUseCaseCommand<Type, Params>
) where Type : Any {
    command.canExecuteLive.observe(
        lifecycleOwner,
        androidx.lifecycle.Observer { canExecute ->
            val isExecuting = command.isExecutingLive.value ?: false
            this.isEnabled = canExecute && isExecuting == false
        })
}

fun <Type> TextView.bindAfterTextChangedWithCommand(
    command: ObservableUseCaseCommand<Type, String>
) where Type : Any {

    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            command.execute(p0?.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    })
}

/**
 * @param inverse if true means view will be visible if [canExecuteLive] is false
 */
fun <Type, Params> View.bindVisibleWithCommandCanExecute(
    lifecycleOwner: LifecycleOwner,
    command: ObservableUseCaseCommand<Type, Params>,
    inverse: Boolean = false
) where Type : Any {
    setVisible(command.canExecuteLive.value, inverse)
    command.canExecuteLive.observe(
        lifecycleOwner,
        androidx.lifecycle.Observer { canExecute ->
            setVisible(canExecute, inverse)
        })
}

fun <Type, Params> View.bindVisibleWithCommandIsExecuting(
    lifecycleOwner: LifecycleOwner,
    command: ObservableUseCaseCommand<Type, Params>,
    inverse: Boolean = false
) where Type : Any {
    setVisible(command.canExecuteLive.value, inverse)
    command.isExecutingLive.observe(
        lifecycleOwner,
        androidx.lifecycle.Observer { isExecuting ->
            setVisible(isExecuting, inverse)
        })
}

private fun View.setVisible(canExecute: Boolean?, inverse: Boolean = false) {
    if (!inverse) {
        this.isVisible = canExecute == true
    } else {
        this.isVisible = canExecute == false
    }
}
