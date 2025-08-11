
package com.example.amonguslfg.data.repository

import com.example.amonguslfg.api.ApiService
import com.example.amonguslfg.model.Announcement
import javax.inject.Inject

import com.example.amonguslfg.model.AnnouncementCreate

class AnnouncementRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getAnnouncements(): List<Announcement> {
        return apiService.getAnnouncements()
    }

    suspend fun createAnnouncement(deviceId: String, announcement: AnnouncementCreate): Announcement {
        return apiService.createAnnouncement(deviceId, announcement)
    }

    suspend fun updateAnnouncement(deviceId: String, id: Int, announcement: AnnouncementCreate): Announcement {
        return apiService.updateAnnouncement(deviceId, id, announcement)
    }

    suspend fun deleteAnnouncement(deviceId: String, id: Int) {
        apiService.deleteAnnouncement(deviceId, id)
    }
}