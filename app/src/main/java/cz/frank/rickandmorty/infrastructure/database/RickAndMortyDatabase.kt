package cz.frank.rickandmorty.infrastructure.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.frank.rickandmorty.infrastructure.database.entity.CharacterEntity
import cz.frank.rickandmorty.infrastructure.database.entity.FavoriteEntity

@Database(entities = [CharacterEntity::class, FavoriteEntity::class], version = 2)
abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
}

fun RickAndMortyDatabase(applicationContext: Context) = Room.databaseBuilder(
    applicationContext,
    RickAndMortyDatabase::class.java, "rick-and-morty"
).build()
