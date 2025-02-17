package cz.frank.rickandmorty.data.source

interface RemoteKeySource {
    suspend fun nextPageToLoad(defaultPageToLoad: Int): Int?
    suspend fun updateNextPage(page: Int?)
}
