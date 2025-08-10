package com.example.amonguslfg.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.RoomRepository
import com.example.amonguslfg.data.repository.UserDataRepository
import com.example.amonguslfg.model.RoomUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class EditRoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userDataRepository: UserDataRepository,
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
            try {
                val room = roomRepository.getRooms().find { it.id == roomId }
                room?.let {
                    roomCode.value = it.room_code
                    currentPlayers.value = it.current_players.toString()
                    maxPlayers.value = it.max_players.toFloat()
                    regulation.value = it.regulation
                    mod.value = it.mod_name
                    server.value = it.server_name
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
                val deviceId = userDataRepository.deviceIdFlow.first()
                roomRepository.updateRoom(deviceId, roomId, roomToUpdate)
                _updateSuccess.value = true
            } catch (e: Exception) {
                Log.e("EditRoomViewModel", "Error updating room", e)
            }
        }
    }
}
