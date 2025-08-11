package com.example.amonguslfg.model

import java.util.Date

data class Announcement(
    val id: Int,
    val title: String,
    val content: String,
    val createdAt: Date
)