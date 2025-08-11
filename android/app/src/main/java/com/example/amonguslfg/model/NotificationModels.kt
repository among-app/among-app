package com.example.amonguslfg.model

import com.google.gson.annotations.SerializedName

data class FcmTokenUpdate(
    val fcmToken: String
)

data class NotificationFilterCreate(
    val filterType: String,
    val filterValue: String
)

data class NotificationSettingsUpdate(
    val notificationsEnabled: Boolean? = null,
    val filters: List<NotificationFilterCreate>? = null
)
