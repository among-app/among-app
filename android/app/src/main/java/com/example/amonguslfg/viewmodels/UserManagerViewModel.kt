
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
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            try {
                _users.value = userDataRepository.getAllUsers()
            } catch (e: Exception) {
                Log.e("UserManagerViewModel", "Failed to fetch users", e)
            }
        }
    }

    private fun updateUserInList(updatedUser: User) {
        val currentUsers = _users.value.toMutableList()
        val userIndex = currentUsers.indexOfFirst { it.id == updatedUser.id }
        if (userIndex != -1) {
            currentUsers[userIndex] = updatedUser
            _users.value = currentUsers
        }
    }

    fun updateUserAdminStatus(userId: String, isChecked: Boolean) {
        viewModelScope.launch {
            try {
                val update = UserUpdate(isAdmin = isChecked)
                val updatedUser = userDataRepository.updateUser(userId.toInt(), update)
                updateUserInList(updatedUser)
            } catch (e: Exception) {
                Log.e("UserManagerViewModel", "Failed to update admin status", e)
            }
        }
    }

    fun updateUserBanStatus(userId: String, isChecked: Boolean) {
        viewModelScope.launch {
            try {
                val update = UserUpdate(isBanned = isChecked)
                val updatedUser = userDataRepository.updateUser(userId.toInt(), update)
                updateUserInList(updatedUser)
            } catch (e: Exception) {
                Log.e("UserManagerViewModel", "Failed to update ban status", e)
            }
        }
    }

    fun updateUserVerifiedStatus(userId: String, isChecked: Boolean) {
        viewModelScope.launch {
            try {
                val update = UserUpdate(isVerified = isChecked)
                val updatedUser = userDataRepository.updateUser(userId.toInt(), update)
                updateUserInList(updatedUser)
            } catch (e: Exception) {
                Log.e("UserManagerViewModel", "Failed to update verified status", e)
            }
        }
    }
}
