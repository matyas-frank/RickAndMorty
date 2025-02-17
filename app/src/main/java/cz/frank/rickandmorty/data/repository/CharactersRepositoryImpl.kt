package cz.frank.rickandmorty.data.repository

import androidx.paging.*
import cz.frank.rickandmorty.data.local.model.toDomain
import cz.frank.rickandmorty.data.mediator.RemoteCharactersMediator
import cz.frank.rickandmorty.data.mediator.RemotePagingSource
import cz.frank.rickandmorty.data.source.CharactersLocalSource
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharactersRepositoryImpl(
    private val localSource: CharactersLocalSource,
    private val allMediator: RemoteCharactersMediator,
    private val remotePagingSource: (String) -> RemotePagingSource
) : CharactersRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun pagedSearchItems(): Flow<PagingData<CharacterSimple>> {
        return Pager(
            PagingConfig(PAGE_SIZE),
            remoteMediator = allMediator,
            pagingSourceFactory = { localSource.allCharacters() }
        ).flow.map { it.map { it.toDomain() } }
    }

    override fun pagedSearchItems(query: String): Flow<PagingData<CharacterSimple>> {
        return Pager(
            PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { remotePagingSource(query) }
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 50
    }
}
