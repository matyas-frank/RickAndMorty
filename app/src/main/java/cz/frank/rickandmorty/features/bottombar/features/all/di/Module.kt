package cz.frank.rickandmorty.features.bottombar.features.all.di

import cz.frank.rickandmorty.features.bottombar.features.all.ui.AllCharactersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val bottomBarAllCharactersModule = module {
    viewModelOf(::AllCharactersViewModel)
}
