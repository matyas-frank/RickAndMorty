package cz.frank.rickandmorty.data.source

import androidx.paging.PagingSource
import cz.frank.rickandmorty.data.local.model.CharacterSimpleWithFavoriteLocalDto
import cz.frank.rickandmorty.domain.model.CharacterSimple
import kotlinx.coroutines.flow.Flow

interface CharactersLocalSource  {
    fun allCharacters(): PagingSource<Int, CharacterSimpleWithFavoriteLocalDto>
    fun favoriteCharacters(): PagingSource<Int, CharacterSimpleWithFavoriteLocalDto>
    fun allFavoritesFlow() : Flow<List<Long>>
    fun isFavorite(id: Long): Flow<Boolean>
    suspend fun addFavorite(favoriteId: Long)
    suspend fun removeFavorite(favoriteId: Long)
    fun addCharacters(characterEntities: List<CharacterSimple>)
    fun deleteAllCharacters()
    fun deleteFavorites()
    suspend fun <R> withTransaction(block: suspend CharactersLocalSource.() -> R): R
}
