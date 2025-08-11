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
import com.example.amonguslfg.model.UserCreate
import com.example.amonguslfg.model.UserUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class UserDataRepository @Inject constructor(
    private val context: Context,
    private val apiService: ApiService
) {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val DEVICE_ID = stringPreferencesKey("device_id")
    }

    val userNameFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.USER_NAME] ?: "" }

    val deviceIdFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.DEVICE_ID] ?: "" }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { settings -> settings[PreferencesKeys.USER_NAME] = name }
    }

    suspend fun registerAndGetUser() {
        var deviceId = deviceIdFlow.first()
        if (deviceId.isBlank()) {
            deviceId = java.util.UUID.randomUUID().toString()
            context.dataStore.edit { settings -> settings[PreferencesKeys.DEVICE_ID] = deviceId }
        }
        Log.d("UserDataRepository", "Your Device ID is: $deviceId")
        val username = userNameFlow.first().ifBlank { "Anonymous" }
        val userCreate = UserCreate(deviceId = deviceId, username = username)
        _currentUser.value = apiService.register(userCreate)
    }

    suspend fun getUsers(): List<User> {
        val deviceId = deviceIdFlow.first()
        return apiService.getUsers(deviceId)
    }

    suspend fun updateUser(userId: Int, userUpdate: UserUpdate): User {
        val deviceId = deviceIdFlow.first()
        return apiService.updateUser(deviceId, userId, userUpdate)
    }

    suspend fun updateMyUsername(username: String) {
        val deviceId = deviceIdFlow.first()
        val userUpdate = UserUpdate(username = username)
        _currentUser.value = apiService.updateMyUser(deviceId, userUpdate)
    }
}