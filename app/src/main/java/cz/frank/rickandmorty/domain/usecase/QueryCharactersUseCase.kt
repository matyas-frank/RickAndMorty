package cz.frank.rickandmorty.domain.usecase

import androidx.paging.PagingData
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.domain.repository.CharactersRepository
import cz.frank.rickandmorty.utils.UseCaseFlowWithParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface QueryCharactersUseCase : UseCaseFlowWithParams<QueryCharactersUseCaseParams,PagingData<CharacterSimple>> {
    override operator fun invoke(params: QueryCharactersUseCaseParams): Flow<PagingData<CharacterSimple>>
}

class QueryCharactersUseCaseParams(val query: String, val cacheInScope: CoroutineScope)

class QueryCharactersUseCaseImpl(private val charactersRepository: CharactersRepository) : QueryCharactersUseCase {
    override fun invoke(params: QueryCharactersUseCaseParams): Flow<PagingData<CharacterSimple>> = charactersRepository.pagedSearchItems(query = params.query, params.cacheInScope)
}
