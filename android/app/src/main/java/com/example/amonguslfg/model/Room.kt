package com.example.amonguslfg.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Room(
    val id: Int,
    @SerializedName("room_code")
    val roomCode: String,
    @SerializedName("current_players")
    val currentPlayers: Int,
    @SerializedName("max_players")
    val maxPlayers: Int,
    val regulation: String,
    @SerializedName("mod_name")
    val modName: String,
    @SerializedName("server_name")
    val serverName: String,
    val description: String?,
    @SerializedName("created_at")
    val createdAt: Date,
    @SerializedName("is_special")
    val isSpecial: Boolean,
    val owner: User
)