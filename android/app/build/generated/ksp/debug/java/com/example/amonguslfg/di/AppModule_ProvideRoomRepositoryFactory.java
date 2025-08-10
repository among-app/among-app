package com.example.amonguslfg.di;

import com.example.amonguslfg.api.ApiService;
import com.example.amonguslfg.data.repository.RoomRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class AppModule_ProvideRoomRepositoryFactory implements Factory<RoomRepository> {
  private final Provider<ApiService> apiServiceProvider;

  public AppModule_ProvideRoomRepositoryFactory(Provider<ApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public RoomRepository get() {
    return provideRoomRepository(apiServiceProvider.get());
  }

  public static AppModule_ProvideRoomRepositoryFactory create(
      Provider<ApiService> apiServiceProvider) {
    return new AppModule_ProvideRoomRepositoryFactory(apiServiceProvider);
  }

  public static RoomRepository provideRoomRepository(ApiService apiService) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRoomRepository(apiService));
  }
}
