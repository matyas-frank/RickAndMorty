package cz.frank.rickandmorty.infrastructure.source

import cz.frank.rickandmorty.data.source.CharactersRemoteSource
import cz.frank.rickandmorty.domain.model.CharacterSimplePaged
import cz.frank.rickandmorty.infrastructure.remote.CharactersService
import cz.frank.rickandmorty.infrastructure.remote.model.simple.toDomain

class CharactersRemoteSourceImpl(private val charactersService: CharactersService) : CharactersRemoteSource {
    override suspend fun getCharacters(page: Int, query: String?): Result<CharacterSimplePaged> {
        return charactersService.getCharacters(page, query).map { it.toDomain() }
    }
}
