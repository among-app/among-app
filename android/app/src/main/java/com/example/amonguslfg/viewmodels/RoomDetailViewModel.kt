package com.example.amonguslfg.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.RoomRepository
import com.example.amonguslfg.model.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val repository: RoomRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val roomId: Int = checkNotNull(savedStateHandle["roomId"])

    private val _room = MutableStateFlow<Room?>(null)
    val room = _room.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _room.value = repository.getRoomById(roomId)
            } catch (e: Exception) {
                Log.e("RoomDetailViewModel", "Error fetching room", e)
            }
        }
    }
}
