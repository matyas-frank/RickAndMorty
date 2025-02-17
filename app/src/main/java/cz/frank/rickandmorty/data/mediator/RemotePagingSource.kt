package cz.frank.rickandmorty.data.mediator

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cz.frank.rickandmorty.data.source.CharactersRemoteSource
import cz.frank.rickandmorty.domain.model.CharacterSimple

class RemotePagingSource(
    private val remoteSource: CharactersRemoteSource,
    val query: String
) : PagingSource<Int, CharacterSimple>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, CharacterSimple> {
        val nextPageNumber = params.key ?: 1
        val response = remoteSource.getCharacters(nextPageNumber, query)
        return response.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it.characters,
                    prevKey = null, // Only paging forward.
                    nextKey = if (it.nextPage != null) nextPageNumber + 1 else null
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterSimple>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
