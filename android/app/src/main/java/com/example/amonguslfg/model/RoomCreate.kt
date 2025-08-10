
package com.example.amonguslfg.model

import com.google.gson.annotations.SerializedName

data class RoomCreate(
    @SerializedName("owner_name")
    val ownerName: String,
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
    val description: String?
)
