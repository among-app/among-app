
package com.example.amonguslfg.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.RoomRepository
import com.example.amonguslfg.data.repository.UserDataRepository
import com.example.amonguslfg.model.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomListViewModel @Inject constructor(
    private val repository: RoomRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _allRooms = MutableStateFlow<List<Room>>(emptyList())

    private val _sortCondition = MutableStateFlow("すべて")
    val sortCondition = _sortCondition.asStateFlow()

    val userName = userDataRepository.userNameFlow.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val rooms: StateFlow<List<Room>> = combine(_allRooms, _sortCondition) { rooms, condition ->
        if (condition == "すべて") {
            rooms
        } else {
            rooms.filter { it.regulation == condition || it.modName == condition }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchRooms()
    }

    fun fetchRooms() {
        viewModelScope.launch {
            try {
                _allRooms.value = repository.getRooms()
            } catch (e: Exception) {
                Log.e("RoomListViewModel", "Error fetching rooms", e)
                _allRooms.value = emptyList()
            }
        }
    }

    fun updateSortCondition(condition: String) {
        _sortCondition.value = condition
    }

    fun deleteRoom(roomId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteRoom(roomId)
                fetchRooms() // Refresh the list after deletion
            } catch (e: Exception) {
                Log.e("RoomListViewModel", "Error deleting room", e)
            }
        }
    }
}
