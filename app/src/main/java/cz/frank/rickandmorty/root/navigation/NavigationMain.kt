package cz.frank.rickandmorty.root.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import cz.frank.rickandmorty.all.navigation.allCharactersNavGraph
import cz.frank.rickandmorty.favorite.navigation.favoriteCharactersNavGraph

fun NavGraphBuilder.mainNav(
    navHostController: NavHostController,
) {
    allCharactersNavGraph(navHostController)
    favoriteCharactersNavGraph(navHostController)
}
