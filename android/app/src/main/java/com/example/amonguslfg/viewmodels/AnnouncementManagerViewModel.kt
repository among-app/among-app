
package com.example.amonguslfg.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amonguslfg.data.repository.AnnouncementRepository
import com.example.amonguslfg.data.repository.UserDataRepository
import com.example.amonguslfg.model.Announcement
import com.example.amonguslfg.model.AnnouncementCreate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementManagerViewModel @Inject constructor(
    private val repository: AnnouncementRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements = _announcements.asStateFlow()

    init {
        fetchAnnouncements()
    }

    fun fetchAnnouncements() {
        viewModelScope.launch {
            try {
                _announcements.value = repository.getAnnouncements()
            } catch (e: Exception) {
                Log.e("AnnouncementsViewModel", "Error fetching announcements", e)
            }
        }
    }

    fun createAnnouncement(title: String, content: String) {
        viewModelScope.launch {
            try {
                val deviceId = userDataRepository.deviceIdFlow.first()
                repository.createAnnouncement(deviceId, AnnouncementCreate(title, content))
                fetchAnnouncements() // Refresh list
            } catch (e: Exception) {
                Log.e("AnnouncementsViewModel", "Error creating announcement", e)
            }
        }
    }

    fun deleteAnnouncement(id: Int) {
        viewModelScope.launch {
            try {
                val deviceId = userDataRepository.deviceIdFlow.first()
                repository.deleteAnnouncement(deviceId, id)
                fetchAnnouncements() // Refresh list
            } catch (e: Exception) {
                Log.e("AnnouncementsViewModel", "Error deleting announcement", e)
            }
        }
    }
}
