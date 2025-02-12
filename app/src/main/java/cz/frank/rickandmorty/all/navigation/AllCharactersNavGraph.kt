package cz.frank.rickandmorty.all.navigation

import kotlinx.serialization.Serializable

@Serializable
object AllCharactersNavGraph {
    @Serializable
    data object All
    @Serializable
    data object QuerySearch
    @Serializable
    data class Detail(val id: Long)
}
