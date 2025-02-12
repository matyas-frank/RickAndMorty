package cz.frank.rickandmorty.ui.favorite
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
object FavoriteCharactersNavGraph {
    @Serializable
    data object All
    @Serializable
    data class Detail(val id: Long)
}

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
