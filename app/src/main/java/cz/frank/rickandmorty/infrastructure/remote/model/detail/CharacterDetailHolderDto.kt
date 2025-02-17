package cz.frank.rickandmorty.infrastructure.remote.model.detail

import cz.frank.rickandmorty.infrastructure.remote.model.GRAPHQLWrapperRemoteDto
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDetailHolderDto(val character: CharacterDetailDto)

fun GRAPHQLWrapperRemoteDto<CharacterDetailHolderDto>.toDomain() = data.character.toDomain(false)
