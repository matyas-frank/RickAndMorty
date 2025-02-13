package cz.frank.rickandmorty.ui.bottombar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import cz.frank.rickandmorty.ui.bottombar.all.allCharactersNavDestination
import cz.frank.rickandmorty.ui.bottombar.favorite.ui.favoriteCharactersNavDestination

fun NavGraphBuilder.bottomBarNavGraph(
    navHostController: NavHostController,
) {
    allCharactersNavDestination(navHostController)
    favoriteCharactersNavDestination(navHostController)
}
