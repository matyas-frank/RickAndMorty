package cz.frank.rickandmorty.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.frank.rickandmorty.R
import cz.frank.rickandmorty.ui.all.AllCharactersNavGraph
import cz.frank.rickandmorty.ui.favorite.FavoriteCharactersNavGraph

enum class NavBarFeature(val route: Any, @StringRes val titleRes: Int, @DrawableRes val iconRes: Int) {
    All(AllCharactersNavGraph, R.string.all, R.drawable.ic_rick),
    Favorite(FavoriteCharactersNavGraph, R.string.favorites, R.drawable.ic_star)
}