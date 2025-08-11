package com.example.amonguslfg.data.repository

import com.example.amonguslfg.api.ApiService
import com.example.amonguslfg.model.Room
import com.example.amonguslfg.model.RoomCreate
import com.example.amonguslfg.model.RoomUpdate
import javax.inject.Inject

class RoomRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getRooms(): List<Room> {
        return apiService.getRooms()
    }

    suspend fun getRoomById(roomId: Int): Room {
        return apiService.getRoomById(roomId)
    }

    suspend fun createRoom(deviceId: String, room: RoomCreate): Room {
        return apiService.createRoom(deviceId, room)
    }

    import com.example.amonguslfg.model.RoomStatusUpdate

    suspend fun updateRoom(deviceId: String, roomId: Int, room: RoomUpdate): Room {
        return apiService.updateRoom(deviceId, roomId, room)
    }

    suspend fun updateRoomStatus(deviceId: String, roomId: Int, status: String): Room {
        return apiService.updateRoomStatus(deviceId, roomId, RoomStatusUpdate(status))
    }

    suspend fun deleteRoom(deviceId: String, roomId: Int) {
        apiService.deleteRoom(deviceId, roomId)
    }
}
