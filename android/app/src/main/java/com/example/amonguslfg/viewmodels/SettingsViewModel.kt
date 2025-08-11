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

    init {
        viewModelScope.launch {
            _userName.value = userDataRepository.userNameFlow.first()
        }
    }

    fun onUserNameChange(newName: String) {
        _userName.value = newName
    }

    fun saveUserName() {
        viewModelScope.launch {
            try {
                // First, save to local DataStore
                userDataRepository.saveUserName(_userName.value)
                // Then, update the name on the server and refresh the user state
                userDataRepository.updateMyUsername(_userName.value)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Failed to save username", e)
            }
        }
    }
}