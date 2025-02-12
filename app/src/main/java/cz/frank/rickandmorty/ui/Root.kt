package cz.frank.rickandmorty.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cz.frank.rickandmorty.ui.all.AllCharactersNavGraph
import cz.frank.rickandmorty.ui.navigation.mainNav

@Composable
fun Root(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier,
    ) { padding ->
        NavHost(
            navController,
            startDestination = AllCharactersNavGraph,
            Modifier.padding(padding)
        ) {
            mainNav(navController)
        }
    }
}
