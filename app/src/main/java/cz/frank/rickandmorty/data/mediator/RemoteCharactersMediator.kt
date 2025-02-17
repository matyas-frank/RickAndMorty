package cz.frank.rickandmorty.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import cz.frank.rickandmorty.data.local.model.CharacterSimpleWithFavoriteLocalDto
import cz.frank.rickandmorty.data.source.CharactersLocalSource
import cz.frank.rickandmorty.data.source.CharactersRemoteSource
import cz.frank.rickandmorty.data.source.RemoteKeySource


@OptIn(ExperimentalPagingApi::class)
class RemoteCharactersMediator(
    private val localSource: CharactersLocalSource,
    private val remoteKeySource: RemoteKeySource,
    private val remoteSource: CharactersRemoteSource,
) : RemoteMediator<Int, CharacterSimpleWithFavoriteLocalDto>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterSimpleWithFavoriteLocalDto>
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> DEFAULT_PAGE_TO_LOAD
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                remoteKeySource.nextPageToLoad(DEFAULT_PAGE_TO_LOAD) ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
        val result = remoteSource.getCharacters(page = loadKey)
        return result.fold(
            onSuccess = { response ->
                localSource.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        deleteAllCharacters()
                    }

                    remoteKeySource.updateNextPage(if(response.nextPage != null) loadKey + 1 else null)
                    addCharacters(response.characters)
                }
                MediatorResult.Success(endOfPaginationReached =  response.nextPage == null)
            },
            onFailure = { MediatorResult.Error(it) }
        )
    }

    private companion object {
        const val DEFAULT_PAGE_TO_LOAD = 1
    }
}
