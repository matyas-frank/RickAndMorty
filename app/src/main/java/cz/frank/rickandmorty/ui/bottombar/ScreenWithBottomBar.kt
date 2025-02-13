package cz.frank.rickandmorty.ui.bottombar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
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
import cz.frank.rickandmorty.ui.bottombar.all.navigation.AllCharactersNavDestination
import cz.frank.rickandmorty.ui.bottombar.navigation.BottomBarDestination
import cz.frank.rickandmorty.ui.bottombar.navigation.BottomBarElement
import cz.frank.rickandmorty.ui.bottombar.navigation.bottomBarNavGraph

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
    Column(modifier) {
        NavHost(
            navController,
            startDestination = AllCharactersNavDestination,
            Modifier.weight(1f)
        ) {
            bottomBarNavGraph(mainNavController)
        }
        BottomBar(navController)
    }
}


@Composable
private fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        BottomBarElement.entries.forEach { topLevelRoute ->
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
