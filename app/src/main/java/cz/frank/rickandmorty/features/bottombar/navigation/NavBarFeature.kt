package cz.frank.rickandmorty.features.bottombar.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.frank.rickandmorty.R
import cz.frank.rickandmorty.features.bottombar.features.all.navigation.AllCharactersNavDestination
import cz.frank.rickandmorty.features.bottombar.features.favorite.navigation.FavoriteCharactersNavDestination

enum class NavBarFeature(val route: Any, @StringRes val titleRes: Int, @DrawableRes val iconRes: Int) {
    All(AllCharactersNavDestination, R.string.all, R.drawable.ic_rick),
    Favorite(FavoriteCharactersNavDestination, R.string.favorites, R.drawable.ic_star)
}