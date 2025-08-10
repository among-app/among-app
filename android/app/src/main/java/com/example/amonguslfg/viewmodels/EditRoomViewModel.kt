
package com.example.amonguslfg.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.RoomRepository
import com.example.amonguslfg.model.RoomUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class EditRoomViewModel @Inject constructor(
    private val repository: RoomRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val roomId: Int = checkNotNull(savedStateHandle["roomId"])

    val roomCode = MutableStateFlow("")
    val currentPlayers = MutableStateFlow("1")
    val maxPlayers = MutableStateFlow(10f)
    val regulation = MutableStateFlow("")
    val mod = MutableStateFlow("")
    val server = MutableStateFlow("")
    val description = MutableStateFlow("")

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess = _updateSuccess.asStateFlow()

    init {
        viewModelScope.launch {
            // This is not ideal, as we don't have a getRoomById endpoint.
            // We fetch all rooms and find the one we need.
            // A dedicated getRoomById endpoint would be better.
            try {
                val room = repository.getRooms().find { it.id == roomId }
                room?.let {
                    roomCode.value = it.roomCode
                    currentPlayers.value = it.currentPlayers.toString()
                    maxPlayers.value = it.maxPlayers.toFloat()
                    regulation.value = it.regulation
                    mod.value = it.modName
                    server.value = it.serverName
                    description.value = it.description ?: ""
                }
            } catch (e: Exception) {
                Log.e("EditRoomViewModel", "Error fetching room data", e)
            }
        }
    }

    fun updateRoom() {
        viewModelScope.launch {
            try {
                val roomToUpdate = RoomUpdate(
                    currentPlayers = currentPlayers.value.toIntOrNull(),
                    maxPlayers = maxPlayers.value.roundToInt(),
                    regulation = regulation.value,
                    modName = mod.value,
                    serverName = server.value,
                    description = description.value.takeIf { it.isNotBlank() }
                )
                repository.updateRoom(roomId, roomToUpdate)
                _updateSuccess.value = true
            } catch (e: Exception) {
                Log.e("EditRoomViewModel", "Error updating room", e)
            }
        }
    }
}
