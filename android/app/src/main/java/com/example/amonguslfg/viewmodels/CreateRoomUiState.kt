package com.example.amonguslfg.viewmodels

sealed interface CreateRoomUiState {
    object Idle : CreateRoomUiState
    object Loading : CreateRoomUiState
    data class Success(val newRoomId: Int) : CreateRoomUiState
    data class Error(val message: String) : CreateRoomUiState
}
