package cz.frank.rickandmorty.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import cz.frank.rickandmorty.data.local.model.CharacterSimpleWithFavoriteLocalDto
import cz.frank.rickandmorty.data.source.CharactersLocalSource
import cz.frank.rickandmorty.data.source.CharactersRemoteSource
import cz.frank.rickandmorty.data.source.RefreshInformationSource
import cz.frank.rickandmorty.data.source.RemoteKeySource
import kotlinx.datetime.Clock


@OptIn(ExperimentalPagingApi::class)
class RemoteCharactersMediator(
    private val localSource: CharactersLocalSource,
    private val remoteKeySource: RemoteKeySource,
    private val remoteSource: CharactersRemoteSource,
    private val refreshInformationSource: RefreshInformationSource,
) : RemoteMediator<Int, CharacterSimpleWithFavoriteLocalDto>() {

    override suspend fun initialize(): InitializeAction {
        val maxAge = refreshInformationSource.maxAge()
        val timeOfLastRefresh = refreshInformationSource.timeOfLastRefresh()
        return when {
            maxAge == null -> InitializeAction.LAUNCH_INITIAL_REFRESH
            timeOfLastRefresh == null -> InitializeAction.LAUNCH_INITIAL_REFRESH
            timeOfLastRefresh + maxAge < Clock.System.now().toEpochMilliseconds() -> InitializeAction.LAUNCH_INITIAL_REFRESH
            else -> InitializeAction.SKIP_INITIAL_REFRESH
        }
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
                        refreshInformationSource.updateTimeOfLastRefresh(Clock.System.now().toEpochMilliseconds())
                        response.maxAge?.let { refreshInformationSource.updateMaxAge(it) }
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
