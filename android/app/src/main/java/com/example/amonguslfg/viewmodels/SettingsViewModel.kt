package com.example.amonguslfg.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled = _notificationsEnabled.asStateFlow()

    private val _notificationFilters = MutableStateFlow(SortFilters())
    val notificationFilters = _notificationFilters.asStateFlow()

    init {
        viewModelScope.launch {
            _userName.value = userDataRepository.userNameFlow.first()
            // TODO: Fetch and apply current user settings
        }
    }

    fun onUserNameChange(newName: String) {
        _userName.value = newName
    }

    fun saveUserName() {
        viewModelScope.launch {
            try {
                userDataRepository.saveUserName(_userName.value)
                userDataRepository.updateMyUsername(_userName.value)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Failed to save username", e)
            }
        }
    }

    fun onNotificationPreferenceChange(isEnabled: Boolean) {
        _notificationsEnabled.value = isEnabled
        saveNotificationSettings()
    }

    fun updateNotificationFilters(filters: SortFilters) {
        _notificationFilters.value = filters
        saveNotificationSettings()
    }

    private fun saveNotificationSettings() {
        viewModelScope.launch {
            try {
                userDataRepository.updateNotificationSettings(
                    isEnabled = _notificationsEnabled.value,
                    filters = _notificationFilters.value
                )
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Failed to save notification settings", e)
            }
        }
    }
}
