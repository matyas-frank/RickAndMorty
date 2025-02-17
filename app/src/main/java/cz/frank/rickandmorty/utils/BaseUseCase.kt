package cz.frank.rickandmorty.utils

import kotlinx.coroutines.flow.Flow
import kotlin.Result


interface UseCaseFlow<Result> {
    operator fun invoke(): Flow<Result>
}

interface UseCaseFlowWithParams<Params, Result> {
    operator fun invoke(params: Params): Flow<Result>
}

interface UseCaseSuspendWithParams<Params, Res : Any> {
    suspend operator fun invoke(params: Params): Result<Res>
}

interface UseCaseWithParams<Params, Result> {
    suspend operator fun invoke(params: Params): Result
}
