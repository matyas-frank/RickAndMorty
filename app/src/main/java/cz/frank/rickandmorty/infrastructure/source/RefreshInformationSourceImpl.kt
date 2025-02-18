package cz.frank.rickandmorty.infrastructure.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.frank.rickandmorty.data.source.RefreshInformationSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "validity_source")

class RefreshInformationSourceImpl(private val context: Context) : RefreshInformationSource {
    override suspend fun maxAge(): Long? {
        return context.dataStore.data.map { it[MAX_AGE_KEY] }.first()
    }

    override suspend fun updateMaxAge(maxAge: Long) {
        context.dataStore.edit { settings ->
            settings[MAX_AGE_KEY] = maxAge
        }
    }

    override suspend fun timeOfLastRefresh(): Long? {
        return context.dataStore.data.map { it[TIME_OF_LAST_REFRESH_KEY] }.first()
    }

    override suspend fun updateTimeOfLastRefresh(time: Long) {
        context.dataStore.edit { settings ->
            settings[TIME_OF_LAST_REFRESH_KEY] = time
        }
    }

    companion object {
        private val MAX_AGE_KEY = longPreferencesKey("max_age")
        private val TIME_OF_LAST_REFRESH_KEY = longPreferencesKey("last_refresh")
    }
}
