package cz.frank.rickandmorty.favorite.navigation

import kotlinx.serialization.Serializable

@Serializable
object FavoriteCharactersNavGraph {
    @Serializable
    data object All
    @Serializable
    data class Detail(val id: Long)
}
