package cz.frank.rickandmorty.utils

import kotlinx.coroutines.flow.Flow


interface UseCaseFlow<Result> {
    operator fun invoke(): Flow<Result>
}

interface UseCaseFlowWithParams<Params, Result> {
    operator fun invoke(params: Params): Flow<Result>
}
