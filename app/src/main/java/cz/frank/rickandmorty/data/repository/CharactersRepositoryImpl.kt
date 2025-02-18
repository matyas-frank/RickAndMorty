package cz.frank.rickandmorty.data.repository

import androidx.paging.*
import cz.frank.rickandmorty.data.local.model.CharacterSimpleWithFavoriteLocalDto
import cz.frank.rickandmorty.data.local.model.toDomain
import cz.frank.rickandmorty.data.mediator.RemoteCharactersMediator
import cz.frank.rickandmorty.data.source.CharactersLocalSource
import cz.frank.rickandmorty.data.source.CharactersRemoteSource
import cz.frank.rickandmorty.data.source.RemotePagingSource
import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.domain.repository.CharactersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CharactersRepositoryImpl(
    private val localSource: CharactersLocalSource,
    private val remoteSource: CharactersRemoteSource,
    private val allMediator: RemoteCharactersMediator,
    private val remotePagingSource: (String) -> RemotePagingSource
) : CharactersRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun pagedSearchItems(): Flow<PagingData<CharacterSimple>> {
        return Pager(
            PagingConfig(PAGE_SIZE),
            remoteMediator = allMediator,
            pagingSourceFactory = { localSource.allCharacters() }
        ).flow.toDomain()
    }

    override fun pagedSearchItems(query: String, cacheInScope: CoroutineScope): Flow<PagingData<CharacterSimple>> {
        return Pager(
            PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { remotePagingSource(query) }
        ).flow.cachedIn(cacheInScope).combine(localSource.allFavoritesFlow()) { characters, favorites ->
            val favoritesSet = favorites.toSet()
            characters.map { it.copy(isFavorite = it.id in favoritesSet) }
        }
    }

    override fun favoriteItems(): Flow<PagingData<CharacterSimple>> {
        return Pager(
            PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { localSource.favoriteCharacters() }
        ).flow.toDomain()
    }

    override fun detailItem(id: Long, refreshFlow: Flow<Unit>): Flow<Result<Character>> = flow {
        refreshFlow.collect {
            emit(remoteSource.getCharacter(id))
        }
    }.combine(localSource.isFavorite(id)) { character, isFavorite ->
        character.map { it.copy(isFavorite = isFavorite) }
    }

    override suspend fun changeFavoriteStatus(id: Long, isFavorite: Boolean) = with(localSource) {
        if (isFavorite) addFavorite(id) else removeFavorite(id)
    }

    companion object {
        private const val PAGE_SIZE = 50
    }
}

private fun Flow<PagingData<CharacterSimpleWithFavoriteLocalDto>>.toDomain() = map { it.map { it.toDomain() } }
