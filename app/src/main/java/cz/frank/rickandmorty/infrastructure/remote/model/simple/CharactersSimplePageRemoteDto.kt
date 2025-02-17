package cz.frank.rickandmorty.infrastructure.remote.model.simple

import cz.frank.rickandmorty.domain.model.CharacterSimplePaged
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharactersSimplePageRemoteDto(
    @SerialName("info") val info: CharactersSimplePageInfoRemoteDto,
    @SerialName("results") val results: List<CharacterSimpleRemoteDto>,
)

fun CharactersSimplePageRemoteDto.toDomain() = CharacterSimplePaged(info.next, results.map { it.toDomain() })
