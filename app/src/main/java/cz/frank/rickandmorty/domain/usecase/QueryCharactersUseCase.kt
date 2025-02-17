package cz.frank.rickandmorty.domain.usecase

import androidx.paging.PagingData
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.domain.repository.CharactersRepository
import cz.frank.rickandmorty.utils.UseCaseFlowWithParams
import kotlinx.coroutines.flow.Flow

interface QueryCharactersUseCase : UseCaseFlowWithParams<String,PagingData<CharacterSimple>> {
    override operator fun invoke(params: String): Flow<PagingData<CharacterSimple>>
}

class QueryCharactersUseCaseImpl(private val charactersRepository: CharactersRepository) : QueryCharactersUseCase {
    override fun invoke(params: String): Flow<PagingData<CharacterSimple>> = charactersRepository.pagedSearchItems(query = params)
}
