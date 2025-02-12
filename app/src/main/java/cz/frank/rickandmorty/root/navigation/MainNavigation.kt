package cz.frank.rickandmorty.root.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import cz.frank.rickandmorty.bottombar.ui.featuresWithBottomBarNavDestination
import cz.frank.rickandmorty.detail.ui.detailNavDestination
import cz.frank.rickandmorty.search.ui.querySearchedCharactersNavDestination

fun NavGraphBuilder.mainNavigation(
    navHostController: NavHostController,
) {
    featuresWithBottomBarNavDestination(navHostController)
    detailNavDestination(navHostController)
    querySearchedCharactersNavDestination(navHostController)
}
