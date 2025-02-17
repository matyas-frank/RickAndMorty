package cz.frank.rickandmorty.data.source

import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.model.CharacterSimplePaged

interface CharactersRemoteSource {
    suspend fun getCharacters(page: Int, query: String? = null) : Result<CharacterSimplePaged>
    suspend fun getCharacter(id: Long) : Result<Character>
}
