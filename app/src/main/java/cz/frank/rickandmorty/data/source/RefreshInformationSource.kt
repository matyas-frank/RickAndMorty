package cz.frank.rickandmorty.data.source

interface RefreshInformationSource {
    suspend fun maxAge(): Long?
    suspend fun updateMaxAge(maxAge: Long)
    suspend fun timeOfLastRefresh(): Long?
    suspend fun updateTimeOfLastRefresh(time: Long)
}
