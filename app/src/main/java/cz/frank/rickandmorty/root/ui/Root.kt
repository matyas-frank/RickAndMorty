package cz.frank.rickandmorty.root.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cz.frank.rickandmorty.features.bottombar.navigation.BottomBarDestination
import cz.frank.rickandmorty.root.navigation.mainNavigation

@Composable
fun Root(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = BottomBarDestination) {
        mainNavigation(navController)
    }
}