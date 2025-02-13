package cz.frank.rickandmorty.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cz.frank.rickandmorty.ui.bottombar.navigation.BottomBarDestination
import cz.frank.rickandmorty.ui.navigation.rootNavigation

@Composable
fun Root(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = BottomBarDestination) {
        rootNavigation(navController)
    }
}