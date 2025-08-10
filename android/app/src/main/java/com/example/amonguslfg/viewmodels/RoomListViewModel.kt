package com.example.amonguslfg.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.RoomRepository
import com.example.amonguslfg.data.repository.UserDataRepository
import com.example.amonguslfg.model.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SortFilters(
    val regulations: Set<String> = emptySet(),
    val mods: Set<String> = emptySet()
)

@HiltViewModel
class RoomListViewModel @Inject constructor(
    private val repository: RoomRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _allRooms = MutableStateFlow<List<Room>>(emptyList())

    private val _sortFilters = MutableStateFlow(SortFilters())
    val sortFilters = _sortFilters.asStateFlow()

    val userName = userDataRepository.userNameFlow.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val rooms: StateFlow<List<Room>> = combine(_allRooms, _sortFilters) { rooms, filters ->
        rooms.filter { room ->
            val regulationMatch = if (filters.regulations.isNotEmpty()) {
                // For multi-select regulations, check if any of the room's regulations match
                room.regulation.split(",").any { it in filters.regulations }
            } else {
                true // No filter applied
            }

            val modMatch = if (filters.mods.isNotEmpty()) {
                filters.mods.contains(room.mod_name)
            } else {
                true // No filter applied
            }

            regulationMatch && modMatch
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
            }
        }
    }

    fun updateSortFilters(filters: SortFilters) {
        _sortFilters.value = filters
    }

    fun clearSortFilters() {
        _sortFilters.value = SortFilters()
    }

    fun deleteRoom(roomId: Int) {
        viewModelScope.launch {
            try {
                val deviceId = userDataRepository.deviceIdFlow.first()
                repository.deleteRoom(deviceId, roomId)
                fetchRooms() // Refresh the list after deletion
            } catch (e: Exception) {
                Log.e("RoomListViewModel", "Error deleting room", e)
            }
        }
    }
}
