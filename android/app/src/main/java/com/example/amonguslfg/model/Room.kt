package com.example.amonguslfg.model

import java.util.Date

data class Room(
    val id: Int,
    val room_code: String,
    val current_players: Int,
    val max_players: Int,
    val regulation: String,
    val mod_name: String,
    val server_name: String,
    val description: String?,
    val created_at: Date,
    val is_special: Boolean,
    val owner: User
)