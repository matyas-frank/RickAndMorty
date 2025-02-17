package cz.frank.rickandmorty.infrastructure.database

import androidx.paging.PagingSource
import androidx.room.*
import cz.frank.rickandmorty.data.local.model.CharacterSimpleWithFavoriteLocalDto
import cz.frank.rickandmorty.infrastructure.database.entity.CharacterEntity
import cz.frank.rickandmorty.infrastructure.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersDao {
    @Query("SELECT characters.id as id,name,status,image,favorites.id as idFromFavorites FROM characters LEFT JOIN favorites ON characters.id == favorites.id")
    fun allCharacters(): PagingSource<Int, CharacterSimpleWithFavoriteLocalDto>

    @Query("SELECT characters.id as id,name,status,image,favorites.id as idFromFavorites FROM characters LEFT JOIN favorites ON characters.id == favorites.id WHERE idFromFavorites LIKE NOT NULL")
    fun favoriteCharacters(): PagingSource<Int, CharacterSimpleWithFavoriteLocalDto>

    @Query("SELECT * FROM favorites")
    fun allFavorites() : Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(favoriteEntity: FavoriteEntity)

    @Insert
    fun addCharacters(vararg characterEntity: CharacterEntity)

    @Query("DELETE FROM characters")
    fun deleteAllCharacters()

    @Query("DELETE FROM favorites")
    fun deleteFavorites()

    @Query("SELECT * FROM favorites WHERE id LIKE :id LIMIT 1")
    fun isFavorite(id: Long) : Flow<FavoriteEntity?>
}
