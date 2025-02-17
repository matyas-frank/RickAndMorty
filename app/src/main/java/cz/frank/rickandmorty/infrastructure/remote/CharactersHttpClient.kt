package cz.frank.rickandmorty.infrastructure.remote

import android.content.Context
import cz.frank.rickandmorty.utils.remote.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

fun CharactersHttpClient(context: Context? = null) =
    HttpClient(
        host = "rickandmortyapi.com",
        engine = OkHttp.create(),
        context = context
    )