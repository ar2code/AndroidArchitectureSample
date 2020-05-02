package ru.ar2code.business_layer.usecases

import ru.ar2code.business_layer_abstract.usecases.AbstractCredsCheckValidUseCase

class CredsCheckValidUseCase : AbstractCredsCheckValidUseCase() {

    companion object {
        const val MIN_SYMBOLS = 4
    }

    override suspend fun run(params: String?): String {
        val input = params ?: ""
        return if (input.length >= MIN_SYMBOLS) {
            input
        } else {
            ""
        }
    }
}