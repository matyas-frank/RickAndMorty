package cz.frank.rickandmorty.root.domain

data class CharacterSimple(
    val id: Long,
    val name: String,
    val status: String,
    val imageUrl: String,
    val isFavorite: Boolean = false
)
