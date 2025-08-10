
package com.example.amonguslfg.di

import com.example.amonguslfg.api.ApiService
import com.example.amonguslfg.data.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
            .baseUrl("http://10.0.2.2:8000/") // For Android Emulator
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    import android.content.Context
import com.example.amonguslfg.data.repository.UserDataRepository
import dagger.hilt.android.qualifiers.ApplicationContext

    @Provides
    @Singleton
    fun provideUserDataRepository(
        @ApplicationContext context: Context,
        apiService: ApiService
    ): UserDataRepository {
        return UserDataRepository(context, apiService)
    }

    import com.example.amonguslfg.data.repository.AnnouncementRepository

    @Provides
    @Singleton
    fun provideAnnouncementRepository(apiService: ApiService): AnnouncementRepository {
        return AnnouncementRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideRoomRepository(apiService: ApiService): RoomRepository {
        return RoomRepository(apiService)
    }
}
