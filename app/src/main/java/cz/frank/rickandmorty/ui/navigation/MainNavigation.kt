package cz.frank.rickandmorty.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import cz.frank.rickandmorty.ui.bottombar.featuresWithBottomBarNavDestination
import cz.frank.rickandmorty.ui.detail.detailNavDestination
import cz.frank.rickandmorty.ui.search.querySearchedCharactersNavDestination

fun NavGraphBuilder.rootNavigation(
    navHostController: NavHostController,
) {
    featuresWithBottomBarNavDestination(navHostController)
    detailNavDestination(navHostController)
    querySearchedCharactersNavDestination(navHostController)
}
