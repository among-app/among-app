
package com.example.amonguslfg.model

data class User(
    val id: Int,
    val device_id: String,
    val username: String,
    val is_admin: Boolean,
    val is_banned: Boolean,
    val is_verified: Boolean
)
