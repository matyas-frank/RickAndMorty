package cz.frank.rickandmorty.domain.usecase

import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.repository.CharactersRepository
import cz.frank.rickandmorty.utils.UseCaseFlowWithParams
import kotlinx.coroutines.flow.Flow

interface GetDetailCharacterUseCase : UseCaseFlowWithParams<GetDetailCharacterUseCaseParams, Result<Character>>

data class GetDetailCharacterUseCaseParams(val id: Long, val refreshFlow: Flow<Unit>)

class GetDetailCharacterUseCaseImpl(private val repository: CharactersRepository) : GetDetailCharacterUseCase {
    override fun invoke(params: GetDetailCharacterUseCaseParams): Flow<Result<Character>> = repository.detailItem(params.id, params.refreshFlow)
}
