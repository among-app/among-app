
package com.example.amonguslfg.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.UserDataRepository
import com.example.amonguslfg.model.User
import com.example.amonguslfg.model.UserUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserManagerViewModel @Inject constructor(
    private val repository: UserDataRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                _users.value = repository.getUsers()
            } catch (e: Exception) {
                Log.e("UserManagerViewModel", "Error fetching users", e)
            }
        }
    }

    fun updateUser(userId: Int, update: UserUpdate) {
        viewModelScope.launch {
            try {
                repository.updateUser(userId, update)
                fetchUsers() // Refresh list
            } catch (e: Exception) {
                Log.e("UserManagerViewModel", "Error updating user", e)
            }
        }
    }
}
