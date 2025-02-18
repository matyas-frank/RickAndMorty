package cz.frank.rickandmorty.infrastructure.remote

import cz.frank.rickandmorty.infrastructure.remote.model.GRAPHQLWrapperRemoteDto
import cz.frank.rickandmorty.infrastructure.remote.model.detail.CharacterDetailHolderDto
import cz.frank.rickandmorty.infrastructure.remote.model.simple.CharactersSimplePageHolderRemoteDto
import cz.frank.rickandmorty.infrastructure.remote.model.simple.HttpHeaderValues
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.time.Duration.Companion.seconds

class CharactersService(private val client: HttpClient) {
    suspend fun getCharacters(page: Int, query: String? = null): Result<Pair<GRAPHQLWrapperRemoteDto<CharactersSimplePageHolderRemoteDto>, HttpHeaderValues>> = runCatching {
        val response = client.post(GRAPHQL) {
            setBody(buildJsonObject {
                put("query",
                    """{ 
                            characters(page: $page ${ query?.let {  ",filter: { name: \"$it\" }"  } ?: "" }) { 
                                info { next } 
                                results { 
                                    id 
                                    name
                                    status
                                    image 
                                } 
                            }
                        }""".trimMargin())
            })
        }
        val maxAge = response.headers["Cache-Control"]
        ?.split("maxage=")
        ?.getOrNull(1)
        ?.split(",")
        ?.firstOrNull()
        ?.toLongOrNull()
        ?.seconds
        ?.inWholeMilliseconds

        response.body<GRAPHQLWrapperRemoteDto<CharactersSimplePageHolderRemoteDto>>() to HttpHeaderValues(maxAge)
    }

    suspend fun getCharacter(id: Long): Result<GRAPHQLWrapperRemoteDto<CharacterDetailHolderDto>> = runCatching {
        client.post(GRAPHQL) {
            setBody(buildJsonObject {
                put(
                    "query",
                    """{
                                character(id:$id) {
                                    id
                                    name
                                    status
                                    image
                                    species
                                    type
                                    gender
                                    origin {
                                        name
                                    }
                                    location {
                                        name
                                    }
                                }  
                        }""".trimMargin()
                )
            })
        }.body()
    }

    companion object {
        private const val GRAPHQL = "graphql/"
    }
}
