package cz.frank.rickandmorty.di

import cz.frank.rickandmorty.ui.bottombar.all.AllCharactersViewModel
import cz.frank.rickandmorty.ui.search.CharacterSearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val rootModule = module {
    viewModelOf(::AllCharactersViewModel)
    viewModelOf(::CharacterSearchViewModel)
}
