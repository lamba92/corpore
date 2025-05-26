package usecase

import kotlinx.coroutines.flow.StateFlow

fun interface FunctionalUseCase<K, T> {
    suspend fun execute(param: T): K
}

suspend fun <T> FunctionalUseCase<T, Unit>.execute() = execute(Unit)

interface ReactiveUseCase<T> {
    val flow: StateFlow<T>
}
