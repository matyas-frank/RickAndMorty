package cz.frank.rickandmorty.infrastructure.remote.model.detail

import cz.frank.rickandmorty.domain.model.Character
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDetailDto(
    val id: Long,
    val name: String,
    val status: String,
    @SerialName("image")
    val imageUrl: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: CharacterDetailLocationDto,
    val location: CharacterDetailLocationDto,
)

fun CharacterDetailDto.toDomain(isFavorite: Boolean) = Character(id, name, status, imageUrl, isFavorite = isFavorite, species, type.ifBlank { "-" }, gender, origin.name, location.name)
