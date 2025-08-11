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

    // Form states
    val roomCode = MutableStateFlow("")
    val currentPlayers = MutableStateFlow("1")
    val maxPlayers = MutableStateFlow(10f)
    val regulations = MutableStateFlow<List<String>>(emptyList())
    val mod = MutableStateFlow("")
    val modOtherText = MutableStateFlow("")
    val server = MutableStateFlow("")
    val serverOtherText = MutableStateFlow("")
    val description = MutableStateFlow("")

    private val _uiState = MutableStateFlow<CreateRoomUiState>(CreateRoomUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        loadRoomDetails()
    }

    private fun loadRoomDetails() {
        viewModelScope.launch {
            try {
                val room = roomRepository.getRoomById(roomId)
                Log.d("EditRoomViewModel", "Fetched room: $room")
                room?.let {
                    roomCode.value = it.roomCode ?: ""
                    currentPlayers.value = it.currentPlayers.toString()
                    maxPlayers.value = it.maxPlayers.toFloat()
                    regulations.value = it.regulation?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
                    mod.value = it.modName ?: ""
                    server.value = it.serverName ?: ""
                    description.value = it.description ?: ""
                }
            } catch (e: Exception) {
                Log.e("EditRoomViewModel", "Error fetching room data", e)
                _uiState.value = CreateRoomUiState.Error("募集情報の読み込みに失敗しました")
            }
        }
    }
    
    fun onRegulationSelected(regulation: String) {
        val currentSelection = regulations.value.toMutableList()
        if (currentSelection.contains(regulation)) {
            currentSelection.remove(regulation)
        } else {
            currentSelection.add(regulation)
        }
        regulations.value = currentSelection
    }

    fun updateRoom() {
        viewModelScope.launch {
            _uiState.value = CreateRoomUiState.Loading
            try {
                val finalModName = if (mod.value == "その他") modOtherText.value else mod.value
                val finalServerName = if (server.value == "その他") serverOtherText.value else server.value

                val roomToUpdate = RoomUpdate(
                    currentPlayers = currentPlayers.value.toIntOrNull(),
                    maxPlayers = maxPlayers.value.roundToInt(),
                    regulation = regulations.value.joinToString(","),
                    modName = finalModName,
                    serverName = finalServerName,
                    description = description.value.takeIf { it.isNotBlank() }
                )
                val deviceId = userDataRepository.deviceIdFlow.first()
                val updatedRoom = roomRepository.updateRoom(deviceId, roomId, roomToUpdate)
                _uiState.value = CreateRoomUiState.Success(updatedRoom.id)
            } catch (e: Exception) {
                Log.e("EditRoomViewModel", "Error updating room", e)
                _uiState.value = CreateRoomUiState.Error(e.message ?: "更新に失敗しました")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = CreateRoomUiState.Idle
    }
}
