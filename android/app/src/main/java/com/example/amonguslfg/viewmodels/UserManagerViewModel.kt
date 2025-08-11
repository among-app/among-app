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

    private val _updatingUserIds = MutableStateFlow<Set<Int>>(emptySet())
    val updatingUserIds = _updatingUserIds.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                _users.value = userDataRepository.getUsers()
            } catch (e: Exception) {
                Log.e("UserManagerViewModel", "Failed to fetch users", e)
            }
            _isRefreshing.value = false
        }
    }

    private fun updateUserInList(updatedUser: User) {
        _users.value = _users.value.map {
            if (it.id == updatedUser.id) updatedUser else it
        }
    }

    fun updateUser(userId: Int, update: UserUpdate) {
        viewModelScope.launch {
            // Optimistically update the UI before the network call
            val optimisticUser = _users.value.find { it.id == userId }?.let { user ->
                update.isAdmin?.let { user.copy(isAdmin = it) } ?: 
                update.isBanned?.let { user.copy(isBanned = it) } ?: 
                update.isVerified?.let { user.copy(isVerified = it) } ?: user
            }
            if (optimisticUser != null) {
                updateUserInList(optimisticUser)
            }

            _updatingUserIds.value += userId
            try {
                // Then, make the actual network call
                val updatedUser = userDataRepository.updateUser(userId, update)
                // And update the list with the true data from the server
                updateUserInList(updatedUser)
            } catch (e: Exception) {
                Log.e("UserManagerViewModel", "Failed to update user", e)
                // If the call fails, refetch to revert the optimistic update
                fetchUsers()
            } finally {
                _updatingUserIds.value -= userId
            }
        }
    }
}