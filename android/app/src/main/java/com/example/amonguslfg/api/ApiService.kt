package com.example.amonguslfg.api

import com.example.amonguslfg.model.*
import retrofit2.http.*

interface ApiService {

    @POST("register/")
    suspend fun register(@Body userCreate: UserCreate): User

    @GET("users/")
    suspend fun getUsers(@Header("x-device-id") deviceId: String): List<User>

    @PUT("users/me")
    suspend fun updateMyUser(@Header("x-device-id") deviceId: String, @Body userUpdate: UserUpdate): User

    @PUT("users/{id}")
    suspend fun updateUser(@Header("x-device-id") deviceId: String, @Path("id") id: Int, @Body userUpdate: UserUpdate): User

    @PUT("users/me/fcm-token")
    suspend fun updateFcmToken(@Header("x-device-id") deviceId: String, @Body fcmTokenUpdate: FcmTokenUpdate): User

    @PUT("users/me/notification-settings")
    suspend fun updateNotificationSettings(@Header("x-device-id") deviceId: String, @Body settingsUpdate: NotificationSettingsUpdate): User

    @GET("rooms/")
    suspend fun getRooms(): List<Room>

    @GET("rooms/{room_id}")
    suspend fun getRoomById(@Path("room_id") roomId: Int): Room

    @POST("rooms/")
    suspend fun createRoom(@Header("x-device-id") deviceId: String, @Body room: RoomCreate): Room

    @PUT("rooms/{room_id}")
    suspend fun updateRoom(@Header("x-device-id") deviceId: String, @Path("room_id") roomId: Int, @Body room: RoomUpdate): Room

    @PUT("rooms/{room_id}/status")
    suspend fun updateRoomStatus(@Header("x-device-id") deviceId: String, @Path("room_id") roomId: Int, @Body statusUpdate: RoomStatusUpdate): Room

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
