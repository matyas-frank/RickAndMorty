package cz.frank.rickandmorty.root.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import cz.frank.rickandmorty.features.bottombar.ui.featuresWithBottomBarNavDestination
import cz.frank.rickandmorty.features.detail.ui.detailNavDestination
import cz.frank.rickandmorty.features.search.ui.querySearchedCharactersNavDestination

fun NavGraphBuilder.mainNavigation(
    navHostController: NavHostController,
) {
    featuresWithBottomBarNavDestination(navHostController)
    detailNavDestination(navHostController)
    querySearchedCharactersNavDestination(navHostController)
}
