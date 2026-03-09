package be.business.newsapp.domain.usecase

import kotlinx.coroutines.flow.Flow

/**
 * Should use for form validations etc.
 */
interface UseCase<out T : Any, in Params : Any> {
    operator fun invoke(params: Params): Result<T>
}

/**
 * Should use for network calls inside coroutines
 */
interface SuspendableUseCase<out T : Any, in Params : Any> {
    suspend operator fun invoke(params: Params): Result<T>
}

/**
 * Should use for reactive data streams
 */
interface ObservableUseCase<out T : Any, in Params : Any> {
    operator fun invoke(params: Params): Flow<T>
}

interface ObservableUseCaseWithResult<out T : Any, in Params : Any> {
    operator fun invoke(params: Params): Flow<Result<T>>
}
