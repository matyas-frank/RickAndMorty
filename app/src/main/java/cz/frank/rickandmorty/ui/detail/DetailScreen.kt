package cz.frank.rickandmorty.ui.detail

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import cz.frank.rickandmorty.ui.detail.navigation.DetailNavDestination

fun NavGraphBuilder.detailNavDestination(
    navHostController: NavHostController,
) {
    composable<DetailNavDestination> {
        Text("Detail")
    }
}
