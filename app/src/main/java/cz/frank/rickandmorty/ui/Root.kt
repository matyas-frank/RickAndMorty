package cz.frank.rickandmorty.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.frank.rickandmorty.ui.all.AllCharactersNavGraph
import cz.frank.rickandmorty.ui.navigation.NavBarFeature
import cz.frank.rickandmorty.ui.navigation.mainNav

@Composable
fun Root(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier,
        bottomBar = { BottomBar(navController) }
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

@Composable
private fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        NavBarFeature.entries.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = { Icon(painterResource(topLevelRoute.iconRes), contentDescription = stringResource(topLevelRoute.titleRes)) },
                label = { Text(stringResource(topLevelRoute.titleRes)) },
                selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    BottomBar(rememberNavController())
}
