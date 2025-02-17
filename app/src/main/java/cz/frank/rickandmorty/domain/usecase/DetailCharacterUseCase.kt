package cz.frank.rickandmorty.domain.usecase

import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.repository.CharactersRepository
import cz.frank.rickandmorty.utils.UseCaseSuspendWithParams

interface DetailCharacterUseCase : UseCaseSuspendWithParams<Long, Character>

class DetailCharacterUseCaseImpl(private val repository: CharactersRepository) : DetailCharacterUseCase {
    override suspend fun invoke(params: Long): Result<Character> = repository.detailItem(params)
}
