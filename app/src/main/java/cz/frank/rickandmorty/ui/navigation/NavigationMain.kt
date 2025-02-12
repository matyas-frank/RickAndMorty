package cz.frank.rickandmorty.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import cz.frank.rickandmorty.ui.all.allCharactersNavGraph
import cz.frank.rickandmorty.ui.favorite.favoriteCharactersNavGraph

fun NavGraphBuilder.mainNav(
    navHostController: NavHostController,
) {
    allCharactersNavGraph(navHostController)
    favoriteCharactersNavGraph(navHostController)
}
