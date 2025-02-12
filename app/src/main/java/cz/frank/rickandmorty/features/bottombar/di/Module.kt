package cz.frank.rickandmorty.features.bottombar.di

import cz.frank.rickandmorty.features.bottombar.features.all.di.bottomBarAllCharactersModule
import cz.frank.rickandmorty.features.bottombar.features.favorite.di.bottomBarFavoriteCharactersModule
import org.koin.dsl.module

val bottomBarModule = module {
    includes(bottomBarAllCharactersModule)
    includes(bottomBarFavoriteCharactersModule)
}
