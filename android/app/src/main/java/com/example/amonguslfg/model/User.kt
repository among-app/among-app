package com.example.amonguslfg.model

data class User(
    val id: Int,
    val deviceId: String,
    val username: String,
    val isAdmin: Boolean,
    val isBanned: Boolean,
    val isVerified: Boolean,
    val notificationsEnabled: Boolean
)