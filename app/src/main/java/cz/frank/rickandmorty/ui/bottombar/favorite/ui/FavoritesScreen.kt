package cz.frank.rickandmorty.ui.bottombar.favorite.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import cz.frank.rickandmorty.ui.bottombar.favorite.navigation.FavoriteCharactersNavDestination
import cz.frank.rickandmorty.ui.detail.navigation.DetailNavDestination

fun NavGraphBuilder.favoriteCharactersNavDestination(
    navHostController: NavHostController,
) {
    composable<FavoriteCharactersNavDestination> {
        Column {
            Text("Favorites")
            Button({ navHostController.navigate(DetailNavDestination(1)) }) {
                Text("Detail")
            }
        }
    }
}
