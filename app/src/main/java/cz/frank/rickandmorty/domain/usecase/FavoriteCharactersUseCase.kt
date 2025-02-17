package cz.frank.rickandmorty.domain.usecase

import androidx.paging.PagingData
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.domain.repository.CharactersRepository
import cz.frank.rickandmorty.utils.UseCaseFlow
import kotlinx.coroutines.flow.Flow

interface FavoriteCharactersUseCase : UseCaseFlow<PagingData<CharacterSimple>>

class FavoriteCharactersUseCaseImpl(private val repository: CharactersRepository) : FavoriteCharactersUseCase {
    override fun invoke(): Flow<PagingData<CharacterSimple>> = repository.favoriteItems()
}
