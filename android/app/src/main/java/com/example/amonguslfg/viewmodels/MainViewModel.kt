package com.example.amonguslfg.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.UserDataRepository
import com.example.amonguslfg.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userDataRepository: UserDataRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = userDataRepository.currentUser

    init {
        viewModelScope.launch {
            // Ensure user is registered on app start
            userDataRepository.registerAndGetUser()
        }
    }
}