package com.example.amonguslfg.di;

import com.example.amonguslfg.api.ApiService;
import com.example.amonguslfg.data.repository.AnnouncementRepository;
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
public final class AppModule_ProvideAnnouncementRepositoryFactory implements Factory<AnnouncementRepository> {
  private final Provider<ApiService> apiServiceProvider;

  public AppModule_ProvideAnnouncementRepositoryFactory(Provider<ApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public AnnouncementRepository get() {
    return provideAnnouncementRepository(apiServiceProvider.get());
  }

  public static AppModule_ProvideAnnouncementRepositoryFactory create(
      Provider<ApiService> apiServiceProvider) {
    return new AppModule_ProvideAnnouncementRepositoryFactory(apiServiceProvider);
  }

  public static AnnouncementRepository provideAnnouncementRepository(ApiService apiService) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAnnouncementRepository(apiService));
  }
}
