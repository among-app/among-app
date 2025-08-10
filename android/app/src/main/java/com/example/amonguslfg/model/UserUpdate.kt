
package com.example.amonguslfg.model

import com.google.gson.annotations.SerializedName

data class UserUpdate(
    @SerializedName("is_admin")
    val isAdmin: Boolean? = null,
    @SerializedName("is_banned")
    val isBanned: Boolean? = null,
    @SerializedName("is_verified")
    val isVerified: Boolean? = null
)
