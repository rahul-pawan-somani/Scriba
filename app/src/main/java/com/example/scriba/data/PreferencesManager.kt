package com.example.scriba.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to create a DataStore instance for settings.
val Context.dataStore by preferencesDataStore(name = "settings")

/**
 * Manager class for handling user preferences using DataStore.
 *
 * Provides flows for dark mode, user name, user email, and view mode,
 * as well as functions to update these preferences.
 *
 * @param context The application context.
 */
class PreferencesManager(context: Context) {
    // Reference to the DataStore instance.
    private val dataStore = context.dataStore

    companion object {
        // Keys for the stored preferences.
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val VIEW_MODE_KEY = booleanPreferencesKey("view_mode")
    }

    /**
     * Flow to observe the dark mode setting.
     * Defaults to false if the preference is not set.
     */
    val darkModeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false
    }

    /**
     * Flow to observe the user name.
     * Defaults to an empty string if the preference is not set.
     */
    val userNameFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY] ?: ""
    }

    /**
     * Flow to observe the user email.
     * Defaults to an empty string if the preference is not set.
     */
    val userEmailFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY] ?: ""
    }

    /**
     * Flow to observe the view mode setting.
     * Defaults to true (grid view) if the preference is not set.
     */
    val viewModeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[VIEW_MODE_KEY] ?: true  // Default to grid view
    }

    /**
     * Updates the dark mode preference.
     *
     * @param enabled True to enable dark mode; false otherwise.
     */
    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    /**
     * Updates the user name preference.
     *
     * @param name The new user name.
     */
    suspend fun setUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    /**
     * Updates the user email preference.
     *
     * @param email The new user email.
     */
    suspend fun setUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
        }
    }

    /**
     * Updates the view mode preference.
     *
     * @param isGrid True for grid view; false for list view.
     */
    suspend fun setGridView(isGrid: Boolean) {
        dataStore.edit { preferences ->
            preferences[VIEW_MODE_KEY] = isGrid
        }
    }
}