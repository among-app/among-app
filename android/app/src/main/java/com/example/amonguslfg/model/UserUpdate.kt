package com.example.amonguslfg.model

data class UserUpdate(
    val isAdmin: Boolean? = null,
    val isBanned: Boolean? = null,
    val isVerified: Boolean? = null
)
