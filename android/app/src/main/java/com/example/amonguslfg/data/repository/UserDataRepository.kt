package com.example.amonguslfg.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.amonguslfg.api.ApiService
import com.example.amonguslfg.model.User
import com.example.amonguslfg.model.UserUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserDataRepository @Inject constructor(
    private val context: Context,
    private val apiService: ApiService
) {

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val DEVICE_ID = stringPreferencesKey("device_id")
    }

    init {
        Log.d("UserDataRepository", "Repository initialized")
    }

    val userNameFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.USER_NAME] ?: "" }

    val deviceIdFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.DEVICE_ID] ?: "" }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { settings -> settings[PreferencesKeys.USER_NAME] = name }
    }

    suspend fun registerAndGetUser(): User {
        var deviceId = deviceIdFlow.first()
        if (deviceId.isBlank()) {
            deviceId = java.util.UUID.randomUUID().toString()
            context.dataStore.edit { settings -> settings[PreferencesKeys.DEVICE_ID] = deviceId }
        }
        Log.d("UserDataRepository", "Your Device ID is: $deviceId")
        val username = userNameFlow.first().ifBlank { "Anonymous" }
        return apiService.register(mapOf("device_id" to deviceId, "username" to username))
    }

    suspend fun getUsers(): List<User> {
        val deviceId = deviceIdFlow.first()
        return apiService.getUsers(deviceId)
    }

    suspend fun updateUser(userId: Int, userUpdate: UserUpdate): User {
        val deviceId = deviceIdFlow.first()
        return apiService.updateUser(deviceId, userId, userUpdate)
    }
}