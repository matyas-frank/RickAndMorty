package cz.frank.rickandmorty.infrastructure.source

import androidx.paging.PagingSource
import androidx.room.withTransaction
import cz.frank.rickandmorty.data.local.model.CharacterSimpleWithFavoriteLocalDto
import cz.frank.rickandmorty.data.source.CharactersLocalSource
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.infrastructure.database.CharactersDao
import cz.frank.rickandmorty.infrastructure.database.RickAndMortyDatabase
import cz.frank.rickandmorty.infrastructure.database.entity.FavoriteEntity
import cz.frank.rickandmorty.infrastructure.database.entity.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CharactersLocalSourceImpl(
    private val charactersDao: CharactersDao,
    private val database: RickAndMortyDatabase
) : CharactersLocalSource {
    override fun allCharacters(): PagingSource<Int, CharacterSimpleWithFavoriteLocalDto> = charactersDao.allCharacters()

    override suspend fun addFavorite(favoriteId: Long) = withContext(Dispatchers.IO) { charactersDao.addFavorite(FavoriteEntity(favoriteId)) }

    override suspend fun removeFavorite(favoriteId: Long) = withContext(Dispatchers.IO) { charactersDao.removeFavorite(FavoriteEntity(favoriteId)) }

    override fun addCharacters(characterEntities: List<CharacterSimple>) = charactersDao.addCharacters(*characterEntities.map { it.toEntity() }.toTypedArray())

    override fun deleteAllCharacters() = charactersDao.deleteAllCharacters()

    override fun deleteFavorites() = charactersDao.deleteFavorites()

    override suspend fun <T> withTransaction(block: suspend CharactersLocalSource.() -> T): T {
        return database.withTransaction { block() }
    }

    override fun allFavoritesFlow(): Flow<List<Long>> = charactersDao.allFavorites().map { it.map { it.id } }

    override fun favoriteCharacters(): PagingSource<Int, CharacterSimpleWithFavoriteLocalDto> = charactersDao.favoriteCharacters()

    override fun isFavorite(id: Long) = charactersDao.isFavorite(id).map { it != null }
}
