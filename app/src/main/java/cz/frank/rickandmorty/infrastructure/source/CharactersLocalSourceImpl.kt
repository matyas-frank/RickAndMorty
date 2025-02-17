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

class CharactersLocalSourceImpl(
    private val charactersDao: CharactersDao,
    private val database: RickAndMortyDatabase
) : CharactersLocalSource {
    override fun allCharacters(): PagingSource<Int, CharacterSimpleWithFavoriteLocalDto> = charactersDao.allCharacters()

    override fun addFavorite(favoriteId: Long) = charactersDao.addFavorite(FavoriteEntity(favoriteId))

    override fun removeFavorite(favoriteId: Long) = charactersDao.removeFavorite(FavoriteEntity(favoriteId))

    override fun addCharacters(characterEntities: List<CharacterSimple>) = charactersDao.addCharacters(*characterEntities.map { it.toEntity() }.toTypedArray())

    override fun deleteAllCharacters() = charactersDao.deleteAllCharacters()

    override fun deleteFavorites() = charactersDao.deleteFavorites()

    override suspend fun <T> withTransaction(block: suspend CharactersLocalSource.() -> T): T {
        return database.withTransaction { block() }
    }
}
