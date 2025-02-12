package cz.frank.rickandmorty.all.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.allCharactersNavGraph(
    navHostController: NavHostController,
) {
    navigation<AllCharactersNavGraph>(startDestination = AllCharactersNavGraph.All) {
        composable<AllCharactersNavGraph.All> {
            Text("All")
        }
        composable<AllCharactersNavGraph.QuerySearch> {
            Text("Search")
        }
        composable<AllCharactersNavGraph.Detail> {
            Text("Detail")
        }
    }
}
