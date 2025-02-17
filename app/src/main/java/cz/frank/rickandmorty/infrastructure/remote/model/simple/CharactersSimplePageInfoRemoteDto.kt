package cz.frank.rickandmorty.infrastructure.remote.model.simple

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharactersSimplePageInfoRemoteDto(
    @SerialName("next")
    val next: Int?,
)
