package cz.frank.rickandmorty.infrastructure.remote.model.simple

import kotlinx.serialization.Serializable

@Serializable
data class CharactersSimplePageHolderRemoteDto(val characters: CharactersSimplePageRemoteDto)
