package cz.frank.rickandmorty.utils.remote

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val HUNDRED_SECONDS = 100_000L

@Suppress("FunctionNaming")
fun HttpClient(host: String, engine: HttpClientEngine, context: Context? = null) = HttpClient(engine) {
    expectSuccess = true

    install(HttpTimeout) {
        connectTimeoutMillis = HUNDRED_SECONDS
    }

    context?.let {
        install(HttpCache) {
            publicStorage(FileStorage(context.cacheDir))
        }
    }

    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = true
                ignoreUnknownKeys = true
                prettyPrint = true
            },
        )
    }

    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            this.host = host
        }
        contentType(ContentType.Application.Json)
    }
}
