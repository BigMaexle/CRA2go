package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ForceLogin (val context: Context){
    val FORCELOGIN = booleanPreferencesKey("force_login")
    suspend fun switch(){
        context.dataStore.edit { settings ->
            settings[FORCELOGIN] = !(settings[FORCELOGIN] ?: false)
        }
    }
    suspend fun get(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[FORCELOGIN] ?: false

    }
}