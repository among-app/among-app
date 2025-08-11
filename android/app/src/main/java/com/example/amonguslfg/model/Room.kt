package com.example.amonguslfg.model

import java.util.Date

data class Room(
    val id: Int,
    val roomCode: String,
    val currentPlayers: Int,
    val maxPlayers: Int,
    val regulation: String,
    val modName: String,
    val serverName: String,
    val description: String?,
    val createdAt: Date,
    val isSpecial: Boolean,
    val owner: User
)
