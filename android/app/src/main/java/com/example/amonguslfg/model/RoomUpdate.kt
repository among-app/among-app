
package com.example.amonguslfg.model

import com.google.gson.annotations.SerializedName

data class RoomUpdate(
    @SerializedName("current_players")
    val currentPlayers: Int? = null,
    @SerializedName("max_players")
    val maxPlayers: Int? = null,
    val regulation: String? = null,
    @SerializedName("mod_name")
    val modName: String? = null,
    @SerializedName("server_name")
    val serverName: String? = null,
    val description: String? = null
)
