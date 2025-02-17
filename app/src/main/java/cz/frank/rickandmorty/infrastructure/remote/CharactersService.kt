package cz.frank.rickandmorty.infrastructure.remote

import android.util.Log
import cz.frank.rickandmorty.infrastructure.remote.model.simple.GRAPHQLHolderRemoteDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class CharactersService(private val client: HttpClient) {
    suspend fun getCharacters(page: Int, query: String? = null): Result<GRAPHQLHolderRemoteDto> = runCatching {
        client.post(GRAPHQL) {
            setBody(buildJsonObject {
                put("query",
                    """{ 
                            characters(page: $page ${ query?.let {  ",filter: { name: $it }"  } ?: "" }) { 
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
        }.also { Log.d("CharacterService", it.body()) }.body()
    }

    companion object {
        private const val GRAPHQL = "graphql/"
    }
}
