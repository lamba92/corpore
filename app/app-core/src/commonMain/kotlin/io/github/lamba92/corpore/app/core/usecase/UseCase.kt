package io.github.lamba92.corpore.app.core.usecase

fun interface UseCase<K, T> {
    suspend fun execute(param: T): K
}

suspend fun <T> UseCase<T, Unit>.execute() = execute(Unit)
