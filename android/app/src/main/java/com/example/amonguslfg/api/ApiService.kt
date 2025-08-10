
package com.example.amonguslfg.api

import com.example.amonguslfg.model.Room
import retrofit2.http.GET

import com.example.amonguslfg.model.RoomUpdate
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("rooms/")
    suspend fun getRooms(): List<Room>

    @POST("rooms/")
    suspend fun createRoom(@Body room: RoomCreate): Room

    @PUT("rooms/{room_id}")
    suspend fun updateRoom(@Path("room_id") roomId: Int, @Body room: RoomUpdate): Room

    import com.example.amonguslfg.model.Announcement

    import com.example.amonguslfg.model.User
import retrofit2.http.Header

    @DELETE("rooms/{room_id}")
    suspend fun deleteRoom(@Path("room_id") roomId: Int)

    import com.example.amonguslfg.model.AnnouncementCreate

    @GET("announcements/")
    suspend fun getAnnouncements(): List<Announcement>

    @POST("announcements/")
    suspend fun createAnnouncement(@Header("x-device-id") deviceId: String, @Body announcement: AnnouncementCreate): Announcement

    @PUT("announcements/{id}")
    suspend fun updateAnnouncement(@Header("x-device-id") deviceId: String, @Path("id") id: Int, @Body announcement: AnnouncementCreate): Announcement

    import com.example.amonguslfg.model.UserUpdate

    @DELETE("announcements/{id}")
    suspend fun deleteAnnouncement(@Header("x-device-id") deviceId: String, @Path("id") id: Int)

    @GET("users/")
    suspend fun getUsers(@Header("x-device-id") deviceId: String): List<User>

    @PUT("users/{id}")
    suspend fun updateUser(@Header("x-device-id") deviceId: String, @Path("id") id: Int, @Body userUpdate: UserUpdate): User

    @POST("register/")
    suspend fun register(@Body deviceId: Map<String, String>): User
}
