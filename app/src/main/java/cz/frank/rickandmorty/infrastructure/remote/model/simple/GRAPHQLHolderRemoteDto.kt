package cz.frank.rickandmorty.infrastructure.remote.model.simple

import cz.frank.rickandmorty.domain.model.CharacterSimplePaged
import kotlinx.serialization.Serializable

@Serializable
data class GRAPHQLHolderRemoteDto(val data: CharactersSimplePageHolderRemoteDto)

fun GRAPHQLHolderRemoteDto.toDomain() = CharacterSimplePaged(data.characters.info.next, data.characters.results.map { it.toDomain() })