package com.agecalc.android.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.agecalc.android.ui.theme.AppThemeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "age_calculator_settings"
)

class ThemePreferencesRepository(private val context: Context) {

    private object Keys {
        val SELECTED_THEME_ID = stringPreferencesKey("selected_theme_id")
    }

    /**
     * Streams the user's persisted theme selection offline.
     * Defaults to [AppThemeOption.VIOLET_DREAM] for first-time users.
     */
    val selectedThemeFlow: Flow<AppThemeOption> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val storedId = preferences[Keys.SELECTED_THEME_ID]
            AppThemeOption.fromId(storedId)
        }

    suspend fun saveTheme(themeOption: AppThemeOption) {
        context.dataStore.edit { preferences ->
            preferences[Keys.SELECTED_THEME_ID] = themeOption.id
        }
    }
}
