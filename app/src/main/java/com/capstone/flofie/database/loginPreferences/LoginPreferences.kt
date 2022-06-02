package com.capstone.flofie.database.loginPreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginPreferences private constructor(private val loginDataStore : DataStore<Preferences>) {

    private val LOGIN_KEY = booleanPreferencesKey("login_status")

    fun getLoginStatus() : Flow<Boolean> {
        return loginDataStore.data.map { preferences ->
            preferences[LOGIN_KEY] ?: false
        }
    }

    suspend fun saveLoginStatus(isLogin : Boolean) {
        loginDataStore.edit { preferences ->
            preferences[LOGIN_KEY] = isLogin
        }
    }

    companion object {

        private var INSTANCE : LoginPreferences? = null

        fun getInstance(dataStore : DataStore<Preferences>) : LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}