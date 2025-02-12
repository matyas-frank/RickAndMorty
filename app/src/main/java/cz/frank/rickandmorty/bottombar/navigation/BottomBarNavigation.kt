package cz.frank.rickandmorty.bottombar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import cz.frank.rickandmorty.bottombar.features.all.ui.allCharactersNavDestination
import cz.frank.rickandmorty.bottombar.features.favorite.ui.favoriteCharactersNavDestination

fun NavGraphBuilder.bottomBarNavGraph(
    navHostController: NavHostController,
) {
    allCharactersNavDestination(navHostController)
    favoriteCharactersNavDestination(navHostController)
}
