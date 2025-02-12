package cz.frank.rickandmorty.features.bottombar.ui

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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.frank.rickandmorty.features.bottombar.features.all.navigation.AllCharactersNavDestination
import cz.frank.rickandmorty.features.bottombar.navigation.BottomBarDestination
import cz.frank.rickandmorty.features.bottombar.navigation.NavBarFeature
import cz.frank.rickandmorty.features.bottombar.navigation.bottomBarNavGraph

fun NavGraphBuilder.featuresWithBottomBarNavDestination(
    navHostController: NavHostController,
) {
    composable<BottomBarDestination> {
        FeaturesWithBottomBarScreen(navHostController)
    }
}

@Composable
private fun FeaturesWithBottomBarScreen(mainNavController: NavHostController, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier,
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        NavHost(
            navController,
            startDestination = AllCharactersNavDestination,
            Modifier.padding(padding)
        ) {
            bottomBarNavGraph(mainNavController)
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
