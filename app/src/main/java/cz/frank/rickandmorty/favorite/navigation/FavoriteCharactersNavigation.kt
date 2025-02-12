package cz.frank.rickandmorty.favorite.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.favoriteCharactersNavGraph(
    navHostController: NavHostController,
) {
    navigation<FavoriteCharactersNavGraph>(startDestination = FavoriteCharactersNavGraph.All) {
        composable<FavoriteCharactersNavGraph.All> {
            Text("Favorite")
        }
        composable<FavoriteCharactersNavGraph.Detail> {
            Text("Detail")
        }
    }
}
