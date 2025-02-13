package cz.frank.rickandmorty.domain.model

data class Character(
    val id: Long,
    val name: String,
    val status: String,
    val imageUrl: String,
    val isFavorite: Boolean,
    val species: String,
    val type: String,
    val gender: String,
    val origin: String,
    val location: String
)
