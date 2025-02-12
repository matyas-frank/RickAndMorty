package cz.frank.rickandmorty.features.bottombar.features.all.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import cz.frank.rickandmorty.features.bottombar.features.all.navigation.AllCharactersNavDestination
import cz.frank.rickandmorty.features.search.navigation.QuerySearchedCharactersNavDestination

fun NavGraphBuilder.allCharactersNavDestination(
    navHostController: NavHostController,
) {
    composable<AllCharactersNavDestination> {
        Column {
            Text("All")
            Button({ navHostController.navigate(QuerySearchedCharactersNavDestination) }) {
                Text("Query search")
            }
        }
    }
}
