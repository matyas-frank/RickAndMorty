package cz.frank.rickandmorty.root.di

import cz.frank.rickandmorty.features.bottombar.di.bottomBarModule
import cz.frank.rickandmorty.features.detail.di.detailModule
import cz.frank.rickandmorty.features.search.di.searchModule
import org.koin.dsl.module

val rootModule = module {
    includes(bottomBarModule)
    includes(detailModule)
    includes(searchModule)
}
