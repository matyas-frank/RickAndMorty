package cz.frank.rickandmorty.domain.repository

import androidx.paging.PagingData
import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.model.CharacterSimple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun pagedSearchItems(query: String, cacheInScope: CoroutineScope): Flow<PagingData<CharacterSimple>>
    fun pagedSearchItems(): Flow<PagingData<CharacterSimple>>
    fun favoriteItems(): Flow<PagingData<CharacterSimple>>
    fun detailItem(id: Long): Flow<Result<Character>>
    suspend fun changeFavoriteStatus(id: Long, isFavorite: Boolean)
}
