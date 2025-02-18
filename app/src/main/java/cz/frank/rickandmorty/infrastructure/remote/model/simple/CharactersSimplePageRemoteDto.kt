package cz.frank.rickandmorty.infrastructure.remote.model.simple

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharactersSimplePageRemoteDto(
    @SerialName("info") val info: CharactersSimplePageInfoRemoteDto,
    @SerialName("results") val results: List<CharacterSimpleRemoteDto>,
)
