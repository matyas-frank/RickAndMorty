package cz.frank.rickandmorty.utils

import kotlinx.coroutines.flow.Flow


interface UseCaseFlow<Result> {
    operator fun invoke(): Flow<Result>
}
