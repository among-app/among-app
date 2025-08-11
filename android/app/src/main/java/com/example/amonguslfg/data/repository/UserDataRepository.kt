package com.example.amonguslfg.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.amonguslfg.api.ApiService
import com.example.amonguslfg.model.*
import com.example.amonguslfg.viewmodels.SortFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.example.amonguslfg.model.FcmTokenUpdate
import com.example.amonguslfg.model.NotificationFilterCreate
import com.example.amonguslfg.model.NotificationSettings
import com.example.amonguslfg.model.NotificationSettingsUpdate

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

    suspend fun registerAndGetUser(): User {
        var deviceId = deviceIdFlow.first()
        if (deviceId.isBlank()) {
            deviceId = java.util.UUID.randomUUID().toString()
            context.dataStore.edit { settings -> settings[PreferencesKeys.DEVICE_ID] = deviceId }
        }
        val username = userNameFlow.first().ifBlank { "Anonymous" }
        val userCreate = UserCreate(deviceId = deviceId, username = username)
        return apiService.register(userCreate)
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

    suspend fun updateFcmToken(token: String) {
        val deviceId = deviceIdFlow.first()
        if (deviceId.isNotBlank()) {
            apiService.updateFcmToken(deviceId, FcmTokenUpdate(fcmToken = token))
        }
    }

    suspend fun getNotificationSettings(): NotificationSettings {
        val deviceId = deviceIdFlow.first()
        return apiService.getNotificationSettings(deviceId)
    }

    suspend fun updateNotificationSettings(isEnabled: Boolean, filters: SortFilters) {
        val deviceId = deviceIdFlow.first()
        if (deviceId.isNotBlank()) {
            val filterCreates = mutableListOf<NotificationFilterCreate>()
            filters.regulations.forEach { filterCreates.add(NotificationFilterCreate(filterType = "regulation", filterValue = it)) }
            filters.mods.forEach { filterCreates.add(NotificationFilterCreate(filterType = "mod", filterValue = it)) }
            
            val settingsUpdate = NotificationSettingsUpdate(
                notificationsEnabled = isEnabled,
                filters = filterCreates
            )
            apiService.updateNotificationSettings(deviceId, settingsUpdate)
        }
    }
}