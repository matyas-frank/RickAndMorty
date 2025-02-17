package cz.frank.rickandmorty.infrastructure.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites")
data class FavoriteEntity(@PrimaryKey(autoGenerate = false) val id: Long)
