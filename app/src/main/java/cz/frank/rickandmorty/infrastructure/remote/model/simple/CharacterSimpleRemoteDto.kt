package cz.frank.rickandmorty.infrastructure.remote.model.simple

import cz.frank.rickandmorty.domain.model.CharacterSimple
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterSimpleRemoteDto(
    val id: Long,
    val name: String,
    val status: String,
    @SerialName("image")
    val imageUrl: String,
)

fun CharacterSimpleRemoteDto.toDomain() = CharacterSimple(id, name, status, imageUrl, false)
