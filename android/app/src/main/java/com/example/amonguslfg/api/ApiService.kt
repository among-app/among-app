package com.example.amonguslfg.api

import com.example.amonguslfg.model.Announcement
import com.example.amonguslfg.model.AnnouncementCreate
import com.example.amonguslfg.model.Room
import com.example.amonguslfg.model.RoomCreate
import com.example.amonguslfg.model.RoomUpdate
import com.example.amonguslfg.model.User
import com.example.amonguslfg.model.UserUpdate
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

import com.example.amonguslfg.model.UserCreate

interface ApiService {

    @POST("register/")
    suspend fun register(@Body userCreate: UserCreate): User

    @GET("users/")
    suspend fun getUsers(@Header("x-device-id") deviceId: String): List<User>

    @PUT("users/{id}")
    suspend fun updateUser(@Header("x-device-id") deviceId: String, @Path("id") id: Int, @Body userUpdate: UserUpdate): User

    @GET("rooms/")
    suspend fun getRooms(): List<Room>

    @GET("rooms/{room_id}")
    suspend fun getRoomById(@Path("room_id") roomId: Int): Room

    @POST("rooms/")
    suspend fun createRoom(@Header("x-device-id") deviceId: String, @Body room: RoomCreate): Room

    @PUT("rooms/{room_id}")
    suspend fun updateRoom(@Header("x-device-id") deviceId: String, @Path("room_id") roomId: Int, @Body room: RoomUpdate): Room

    @DELETE("rooms/{room_id}")
    suspend fun deleteRoom(@Header("x-device-id") deviceId: String, @Path("room_id") roomId: Int)

    @GET("announcements/")
    suspend fun getAnnouncements(): List<Announcement>

    @POST("announcements/")
    suspend fun createAnnouncement(@Header("x-device-id") deviceId: String, @Body announcement: AnnouncementCreate): Announcement

    @PUT("announcements/{id}")
    suspend fun updateAnnouncement(@Header("x-device-id") deviceId: String, @Path("id") id: Int, @Body announcement: AnnouncementCreate): Announcement

    @DELETE("announcements/{id}")
    suspend fun deleteAnnouncement(@Header("x-device-id") deviceId: String, @Path("id") id: Int)
}
