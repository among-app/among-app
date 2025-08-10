package com.example.amonguslfg.data.repository;

import com.example.amonguslfg.api.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AnnouncementRepository_Factory implements Factory<AnnouncementRepository> {
  private final Provider<ApiService> apiServiceProvider;

  public AnnouncementRepository_Factory(Provider<ApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public AnnouncementRepository get() {
    return newInstance(apiServiceProvider.get());
  }

  public static AnnouncementRepository_Factory create(Provider<ApiService> apiServiceProvider) {
    return new AnnouncementRepository_Factory(apiServiceProvider);
  }

  public static AnnouncementRepository newInstance(ApiService apiService) {
    return new AnnouncementRepository(apiService);
  }
}
