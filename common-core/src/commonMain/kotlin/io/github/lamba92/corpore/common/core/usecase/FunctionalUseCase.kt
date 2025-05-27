package io.github.lamba92.corpore.common.core.usecase

import kotlinx.coroutines.flow.StateFlow

fun interface FunctionalUseCase<IN, OUT> {
    suspend fun execute(param: IN): OUT
}

suspend fun <OUT> FunctionalUseCase<Unit, OUT>.execute() = execute(Unit)

interface ReactiveUseCase<T> {
    val flow: StateFlow<T>
}
