package cz.frank.rickandmorty.domain.usecase

import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.repository.CharactersRepository
import cz.frank.rickandmorty.utils.UseCaseFlowWithParams
import kotlinx.coroutines.flow.Flow

interface GetDetailCharacterUseCase : UseCaseFlowWithParams<Long, Result<Character>>

class DetailCharacterUseCaseImpl(private val repository: CharactersRepository) : GetDetailCharacterUseCase {
    override fun invoke(params: Long): Flow<Result<Character>> = repository.detailItem(params)
}
