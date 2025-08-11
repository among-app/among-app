package com.example.amonguslfg.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    @SerializedName("device_id")
    val deviceId: String,
    val username: String,
    @SerializedName("is_admin")
    val isAdmin: Boolean,
    @SerializedName("is_banned")
    val isBanned: Boolean,
    @SerializedName("is_verified")
    val isVerified: Boolean
)