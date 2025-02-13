package cz.frank.rickandmorty.di

import cz.frank.rickandmorty.ui.bottombar.all.AllCharactersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val rootModule = module {
    viewModelOf(::AllCharactersViewModel)
}
