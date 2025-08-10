
package com.example.amonguslfg.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.RoomRepository
import com.example.amonguslfg.model.RoomCreate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

import androidx.lifecycle.SavedStateHandle

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userDataRepository: UserDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val isSpecial = MutableStateFlow(savedStateHandle.get<Boolean>("isSpecial") ?: false)

    val roomCode = MutableStateFlow("")
    val currentPlayers = MutableStateFlow("1")
    val maxPlayers = MutableStateFlow(10f)
    val regulation = MutableStateFlow("")
    val mod = MutableStateFlow("")
    val server = MutableStateFlow("")
    val description = MutableStateFlow("")

    private val _creationSuccess = MutableStateFlow(false)
    val creationSuccess = _creationSuccess.asStateFlow()

    fun createRoom() {
        viewModelScope.launch {
            try {
                val ownerName = userDataRepository.userNameFlow.first().ifBlank { "Anonymous" }
                val roomToCreate = RoomCreate(
                    ownerName = ownerName,
                    roomCode = roomCode.value,
                    currentPlayers = currentPlayers.value.toIntOrNull() ?: 1,
                    maxPlayers = maxPlayers.value.roundToInt(),
                    regulation = regulation.value,
                    modName = mod.value,
                    serverName = server.value,
                    description = description.value.takeIf { it.isNotBlank() },
                    is_special = isSpecial
                )
                roomRepository.createRoom(roomToCreate)
                _creationSuccess.value = true
            } catch (e: Exception) {
                Log.e("CreateRoomViewModel", "Error creating room", e)
                _creationSuccess.value = false
            }
        }
    }
}

