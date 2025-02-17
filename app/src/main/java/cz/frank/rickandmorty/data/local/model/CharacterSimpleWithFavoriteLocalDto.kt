package cz.frank.rickandmorty.data.local.model

import cz.frank.rickandmorty.domain.model.CharacterSimple

data class CharacterSimpleWithFavoriteLocalDto(
    val id: Long,
    val name: String,
    val status: String,
    val image: String,
    val idFromFavorites: Int?,
)
fun CharacterSimpleWithFavoriteLocalDto.toDomain() = CharacterSimple(id, name, status, image, idFromFavorites != null)
