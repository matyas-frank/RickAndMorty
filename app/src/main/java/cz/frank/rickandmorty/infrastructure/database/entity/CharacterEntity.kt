package cz.frank.rickandmorty.infrastructure.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.frank.rickandmorty.domain.model.CharacterSimple

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val status: String,
    val image: String,
)

fun CharacterSimple.toEntity() = CharacterEntity(id, name, status, imageUrl)
