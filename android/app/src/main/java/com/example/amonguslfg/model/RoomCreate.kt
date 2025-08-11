package com.example.amonguslfg.model

data class RoomCreate(
    val roomCode: String,
    val currentPlayers: Int,
    val maxPlayers: Int,
    val regulation: List<String>,
    val modName: String,
    val serverName: String,
    val description: String?,
    val isSpecial: Boolean = false
)
