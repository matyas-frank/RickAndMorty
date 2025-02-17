package cz.frank.rickandmorty.infrastructure.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.frank.rickandmorty.data.source.RemoteKeySource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "characters_remote_key_source")

open class RemoteKeySourceImpl(private val context: Context) : RemoteKeySource {
    override suspend fun nextPageToLoad(defaultPageToLoad: Int): Int? {
        val page = context.dataStore.data.map { it[PAGE_KEY] }.first()
        return when(page) {
            null -> defaultPageToLoad
            NO_NEXT_PAGE_INDICATOR -> null
            else -> page
        }
    }

    override suspend fun updateNextPage(page: Int?) {
        context.dataStore.edit { settings ->
            settings[PAGE_KEY] = page ?: NO_NEXT_PAGE_INDICATOR
        }
    }

    companion object {
        private const val NO_NEXT_PAGE_INDICATOR = -1
        private val PAGE_KEY = intPreferencesKey("page")
    }
}
