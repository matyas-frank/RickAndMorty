package cz.frank.rickandmorty.domain.repository

import androidx.paging.PagingData
import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.model.CharacterSimple
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun pagedSearchItems(query: String): Flow<PagingData<CharacterSimple>>
    fun pagedSearchItems(): Flow<PagingData<CharacterSimple>>
    fun favoriteItems(): Flow<PagingData<CharacterSimple>>
    suspend fun detailItem(id: Long): Result<Character>
}
