package cz.frank.rickandmorty.search.ui

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import cz.frank.rickandmorty.search.navigation.QuerySearchedCharactersNavDestination

fun NavGraphBuilder.querySearchedCharactersNavDestination(
    navHostController: NavHostController,
) {
    composable<QuerySearchedCharactersNavDestination> {
        Text("Query search")
    }
}
