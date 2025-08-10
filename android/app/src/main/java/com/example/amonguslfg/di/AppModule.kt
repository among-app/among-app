package com.example.amonguslfg.di

import android.content.Context
import com.example.amonguslfg.api.ApiService
import com.example.amonguslfg.data.repository.AnnouncementRepository
import com.example.amonguslfg.data.repository.RoomRepository
import com.example.amonguslfg.data.repository.UserDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://relieved-roselin-plaplapla-7cc9aab5.koyeb.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserDataRepository(
        @ApplicationContext context: Context,
        apiService: ApiService
    ): UserDataRepository {
        return UserDataRepository(context, apiService)
    }

    @Provides
    @Singleton
    fun provideRoomRepository(apiService: ApiService): RoomRepository {
        return RoomRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideAnnouncementRepository(apiService: ApiService): AnnouncementRepository {
        return AnnouncementRepository(apiService)
    }
}
