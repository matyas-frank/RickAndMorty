package cz.frank.rickandmorty.ui.all
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
object AllCharactersNavGraph {
    @Serializable
    data object All
    @Serializable
    data object QuerySearch
    @Serializable
    data class Detail(val id: Long)
}

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
