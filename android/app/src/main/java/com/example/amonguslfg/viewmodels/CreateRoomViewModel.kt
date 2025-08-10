package com.example.amonguslfg.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.RoomRepository
import com.example.amonguslfg.data.repository.UserDataRepository
import com.example.amonguslfg.model.RoomCreate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

import retrofit2.HttpException

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
    val regulations = MutableStateFlow<List<String>>(emptyList())
    val mod = MutableStateFlow("")
    val modOtherText = MutableStateFlow("")
    val server = MutableStateFlow("")
    val serverOtherText = MutableStateFlow("")
    val description = MutableStateFlow("")

    private val _uiState = MutableStateFlow<CreateRoomUiState>(CreateRoomUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onRoomCodeChange(newCode: String) {
        if (newCode.length <= 6) {
            roomCode.value = newCode.uppercase()
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

    private fun validateInputs(): Boolean {
        if (roomCode.value.length != 6) {
            _uiState.value = CreateRoomUiState.Error("ルームコードは6文字で入力してください")
            return false
        }
        if (regulations.value.isEmpty()) {
            _uiState.value = CreateRoomUiState.Error("レギュレーションが選択されていません")
            return false
        }
        if (mod.value.isBlank() || (mod.value == "その他" && modOtherText.value.isBlank())) {
            _uiState.value = CreateRoomUiState.Error("MODが選択または入力されていません")
            return false
        }
        if (server.value.isBlank() || (server.value == "その他" && serverOtherText.value.isBlank())) {
            _uiState.value = CreateRoomUiState.Error("サーバーが選択または入力されていません")
            return false
        }
        return true
    }

    fun createRoom() {
        if (!validateInputs()) return

        viewModelScope.launch {
            _uiState.value = CreateRoomUiState.Loading
            try {
                val finalModName = if (mod.value == "その他") modOtherText.value else mod.value
                val finalServerName = if (server.value == "その他") serverOtherText.value else server.value

                val roomToCreate = RoomCreate(
                    roomCode = roomCode.value,
                    currentPlayers = currentPlayers.value.toIntOrNull() ?: 1,
                    maxPlayers = maxPlayers.value.roundToInt(),
                    regulation = regulations.value,
                    modName = finalModName,
                    serverName = finalServerName,
                    description = description.value.takeIf { it.isNotBlank() },
                    isSpecial = isSpecial.value
                )
                val deviceId = userDataRepository.deviceIdFlow.first()
                roomRepository.createRoom(deviceId, roomToCreate)
                _uiState.value = CreateRoomUiState.Success
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: "不明なエラー"
                Log.e("CreateRoomViewModel", "Error creating room: $errorBody", e)
                _uiState.value = CreateRoomUiState.Error(errorBody)
            } catch (e: Exception) {
                Log.e("CreateRoomViewModel", "Error creating room", e)
                _uiState.value = CreateRoomUiState.Error(e.message ?: "不明なエラーが発生しました")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = CreateRoomUiState.Idle
    }
}