package cz.frank.rickandmorty.domain.usecase

import androidx.paging.PagingData
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow

interface UseCaseFlow<Result> {
    operator fun invoke(): Flow<Result>
}

interface AllCharactersUseCase : UseCaseFlow<PagingData<CharacterSimple>> {
    override operator fun invoke(): Flow<PagingData<CharacterSimple>>
}

class AlLCharactersUseCaseImpl(private val charactersRepository: CharactersRepository) : AllCharactersUseCase {
    override fun invoke(): Flow<PagingData<CharacterSimple>> = charactersRepository.pagedSearchItems()
}
