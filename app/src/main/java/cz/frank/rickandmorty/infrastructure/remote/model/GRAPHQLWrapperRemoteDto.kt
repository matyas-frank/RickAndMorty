package cz.frank.rickandmorty.infrastructure.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GRAPHQLWrapperRemoteDto<DATA: Any>(val data: DATA)
