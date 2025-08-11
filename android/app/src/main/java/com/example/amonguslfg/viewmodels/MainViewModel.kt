package com.example.amonguslfg.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.UserDataRepository
import com.example.amonguslfg.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.util.Log

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = userDataRepository.currentUser

    fun initializeUser() {
        viewModelScope.launch {
            // Step 1: Ensure a local name exists, creating one if it doesn't.
            var localUserName = userDataRepository.userNameFlow.first()
            if (localUserName.isBlank()) {
                localUserName = "Player${(1000..9999).random()}"
                userDataRepository.saveUserName(localUserName)
            }

            try {
                // Step 2: Try to update the user. This works for existing users.
                Log.d("MainViewModel", "Attempting to update user (for existing users)...")
                userDataRepository.updateMyUsername(localUserName)
                Log.d("MainViewModel", "Update successful.")
            } catch (e: Exception) {
                Log.w("MainViewModel", "Update failed, assuming new user. Falling back to registration.", e)
                // Step 3: If update fails, assume user is new and register them.
                try {
                    userDataRepository.registerAndGetUser()
                    Log.d("MainViewModel", "Registration successful.")
                } catch (e2: Exception) {
                    Log.e("MainViewModel", "Fatal: Both update and registration failed.", e2)
                }
            }
        }
    }
}