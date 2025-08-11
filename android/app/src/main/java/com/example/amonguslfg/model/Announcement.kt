package com.example.amonguslfg.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Announcement(
    val id: Int,
    val title: String,
    val content: String,
    @SerializedName("created_at")
    val createdAt: Date
)