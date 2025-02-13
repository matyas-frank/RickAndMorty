package cz.frank.rickandmorty.di

import cz.frank.rickandmorty.ui.bottombar.all.AllCharactersViewModel
import cz.frank.rickandmorty.ui.bottombar.favorite.ui.FavoriteCharactersViewModel
import cz.frank.rickandmorty.ui.detail.DetailCharacterViewModel
import cz.frank.rickandmorty.ui.search.QuerySearchCharactersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val rootModule = module {
    viewModelOf(::AllCharactersViewModel)
    viewModelOf(::QuerySearchCharactersViewModel)
    viewModelOf(::FavoriteCharactersViewModel)
    viewModelOf(::DetailCharacterViewModel)
}
