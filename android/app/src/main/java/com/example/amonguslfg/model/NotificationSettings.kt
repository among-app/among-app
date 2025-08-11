package com.example.amonguslfg.model

data class NotificationFilter(
    val id: Int,
    val filterType: String,
    val filterValue: String
)

data class NotificationSettings(
    val notificationsEnabled: Boolean,
    val filters: List<NotificationFilter>
)
