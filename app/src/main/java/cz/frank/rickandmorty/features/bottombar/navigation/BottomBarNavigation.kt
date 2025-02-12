package cz.frank.rickandmorty.features.bottombar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import cz.frank.rickandmorty.features.bottombar.features.all.ui.allCharactersNavDestination
import cz.frank.rickandmorty.features.bottombar.features.favorite.ui.favoriteCharactersNavDestination

fun NavGraphBuilder.bottomBarNavGraph(
    navHostController: NavHostController,
) {
    allCharactersNavDestination(navHostController)
    favoriteCharactersNavDestination(navHostController)
}
