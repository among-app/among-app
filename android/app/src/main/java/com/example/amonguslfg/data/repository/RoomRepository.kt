
package com.example.amonguslfg.data.repository

import com.example.amonguslfg.api.ApiService
import com.example.amonguslfg.model.Room

import com.example.amonguslfg.model.RoomUpdate

class RoomRepository(private val apiService: ApiService) {
    suspend fun getRooms(): List<Room> {
        return apiService.getRooms()
    }

    suspend fun createRoom(room: RoomCreate): Room {
        return apiService.createRoom(room)
    }

    suspend fun updateRoom(roomId: Int, room: RoomUpdate): Room {
        return apiService.updateRoom(roomId, room)
    }

    suspend fun deleteRoom(roomId: Int) {
        apiService.deleteRoom(roomId)
    }
}
