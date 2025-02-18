package cz.frank.rickandmorty.infrastructure.source

import cz.frank.rickandmorty.data.source.CharactersRemoteSource
import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.model.CharacterSimplePaged
import cz.frank.rickandmorty.infrastructure.remote.CharactersService
import cz.frank.rickandmorty.infrastructure.remote.model.detail.toDomain
import cz.frank.rickandmorty.infrastructure.remote.model.simple.toDomain

class CharactersRemoteSourceImpl(private val charactersService: CharactersService) : CharactersRemoteSource {
    override suspend fun getCharacters(page: Int, query: String?): Result<CharacterSimplePaged> {
        return charactersService.getCharacters(page, query).map { (dto, header) ->
            dto.toDomain(header.maxAge)
        }
    }

    override suspend fun getCharacter(id: Long): Result<Character> {
        return charactersService.getCharacter(id).map { it.toDomain() }
    }
}
