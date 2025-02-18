package cz.frank.rickandmorty.infrastructure.remote.model.simple

import cz.frank.rickandmorty.domain.model.CharacterSimplePaged
import cz.frank.rickandmorty.infrastructure.remote.model.GRAPHQLWrapperRemoteDto
import kotlinx.serialization.Serializable

@Serializable
data class CharactersSimplePageHolderRemoteDto(val characters: CharactersSimplePageRemoteDto)

fun GRAPHQLWrapperRemoteDto<CharactersSimplePageHolderRemoteDto>.toDomain(maxAge: Long?) =
    CharacterSimplePaged(data.characters.info.next, data.characters.results.map { it.toDomain() }, maxAge)
