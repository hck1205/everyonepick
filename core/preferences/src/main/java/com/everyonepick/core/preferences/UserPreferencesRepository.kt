package com.everyonepick.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.everyonepick.core.common.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Singleton
class UserPreferencesRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) {
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = CoroutineScope(ioDispatcher + SupervisorJob()),
        produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES_FILE) },
    )

    val userPreferences: Flow<UserPreferences> =
        dataStore.data
            .catch { throwable ->
                if (throwable is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw throwable
                }
            }
            .map { preferences ->
                UserPreferences(
                    themeMode = ThemeMode.fromStorageValue(preferences[Keys.themeMode]),
                )
            }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[Keys.themeMode] = themeMode.storageValue
        }
    }

    private object Keys {
        val themeMode = stringPreferencesKey("theme_mode")
    }

    private companion object {
        const val USER_PREFERENCES_FILE = "user_preferences.preferences_pb"
    }
}

