package cz.frank.rickandmorty.domain.usecase

import cz.frank.rickandmorty.domain.repository.CharactersRepository
import cz.frank.rickandmorty.utils.UseCaseWithParams

interface ChangeFavoriteStatusUseCase : UseCaseWithParams<ChangeFavoriteStatusUseCaseParams, Unit>

data class ChangeFavoriteStatusUseCaseParams(val id: Long, val isFavorite: Boolean)

class ChangeFavoriteStatusUseCaseImpl(private val repository: CharactersRepository) : ChangeFavoriteStatusUseCase {
    override suspend fun invoke(params: ChangeFavoriteStatusUseCaseParams) {
        repository.changeFavoriteStatus(params.id, params.isFavorite)
    }
}
