package cz.frank.rickandmorty.di

import cz.frank.rickandmorty.data.mediator.RemoteCharactersMediator
import cz.frank.rickandmorty.data.repository.CharactersRepositoryImpl
import cz.frank.rickandmorty.data.source.*
import cz.frank.rickandmorty.domain.repository.CharactersRepository
import cz.frank.rickandmorty.domain.usecase.*
import cz.frank.rickandmorty.infrastructure.database.RickAndMortyDatabase
import cz.frank.rickandmorty.infrastructure.remote.CharactersHttpClient
import cz.frank.rickandmorty.infrastructure.remote.CharactersService
import cz.frank.rickandmorty.infrastructure.source.CharactersLocalSourceImpl
import cz.frank.rickandmorty.infrastructure.source.CharactersRemoteSourceImpl
import cz.frank.rickandmorty.infrastructure.source.RemoteKeySourceImpl
import cz.frank.rickandmorty.infrastructure.source.RefreshInformationSourceImpl
import cz.frank.rickandmorty.ui.bottombar.all.AllCharactersViewModel
import cz.frank.rickandmorty.ui.bottombar.favorite.ui.FavoriteCharactersViewModel
import cz.frank.rickandmorty.ui.detail.DetailCharacterViewModel
import cz.frank.rickandmorty.ui.search.QuerySearchCharactersViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val rootModule = module {
    source()
    repositories()
    useCases()
    viewModels()
}

private fun Module.source() {
    localSource()
    remoteSource()
    single { { query: String -> RemotePagingSource(get(), query) } }
    singleOf(::RemoteCharactersMediator)
}

private fun Module.localSource() {
    single { RickAndMortyDatabase(get()) }
    single { get<RickAndMortyDatabase>().charactersDao() }
    singleOf(::CharactersLocalSourceImpl) bind CharactersLocalSource::class
    singleOf(::RefreshInformationSourceImpl) bind RefreshInformationSource::class
}

private fun Module.remoteSource() {
    single { CharactersHttpClient(get()) }
    singleOf(::CharactersService)
    singleOf(::CharactersRemoteSourceImpl) bind CharactersRemoteSource::class
    singleOf(::RemoteKeySourceImpl) bind RemoteKeySource::class
}

private fun Module.repositories() {
    singleOf(::CharactersRepositoryImpl) bind CharactersRepository::class
}

private fun Module.useCases() {
    singleOf(::AlLCharactersUseCaseImpl) bind AllCharactersUseCase::class
    singleOf(::QueryCharactersUseCaseImpl) bind QueryCharactersUseCase::class
    singleOf(::FavoriteCharactersUseCaseImpl) bind FavoriteCharactersUseCase::class
    singleOf(::GetDetailCharacterUseCaseImpl) bind GetDetailCharacterUseCase::class
    singleOf(::ChangeFavoriteStatusUseCaseImpl) bind ChangeFavoriteStatusUseCase::class
}


private fun Module.viewModels() {
    viewModelOf(::AllCharactersViewModel)
    viewModelOf(::QuerySearchCharactersViewModel)
    viewModelOf(::FavoriteCharactersViewModel)
    viewModelOf(::DetailCharacterViewModel)
}
