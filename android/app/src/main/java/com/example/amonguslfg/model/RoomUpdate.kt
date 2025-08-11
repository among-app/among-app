package com.example.amonguslfg.model

data class RoomUpdate(
    val currentPlayers: Int? = null,
    val maxPlayers: Int? = null,
    val regulation: String? = null,
    val modName: String? = null,
    val serverName: String? = null,
    val description: String? = null,
    val isSpecial: Boolean? = null
)
