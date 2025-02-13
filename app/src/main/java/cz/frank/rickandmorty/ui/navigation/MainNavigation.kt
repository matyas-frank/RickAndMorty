package cz.frank.rickandmorty.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import cz.frank.rickandmorty.ui.bottombar.featuresWithBottomBarNavDestination
import cz.frank.rickandmorty.ui.detail.detailCharacterNavDestination
import cz.frank.rickandmorty.ui.search.querySearchCharactersNavDestination

fun NavGraphBuilder.rootNavigation(
    navHostController: NavHostController,
) {
    featuresWithBottomBarNavDestination(navHostController)
    detailCharacterNavDestination(navHostController)
    querySearchCharactersNavDestination(navHostController)
}
