package com.example.amonguslfg.viewmodels

sealed interface CreateRoomUiState {
    object Idle : CreateRoomUiState
    object Loading : CreateRoomUiState
    object Success : CreateRoomUiState
    data class Error(val message: String) : CreateRoomUiState
}
