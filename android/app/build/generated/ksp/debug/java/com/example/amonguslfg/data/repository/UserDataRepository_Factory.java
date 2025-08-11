package com.example.amonguslfg.data.repository;

import android.content.Context;
import com.example.amonguslfg.api.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class UserDataRepository_Factory implements Factory<UserDataRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<ApiService> apiServiceProvider;

  public UserDataRepository_Factory(Provider<Context> contextProvider,
      Provider<ApiService> apiServiceProvider) {
    this.contextProvider = contextProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public UserDataRepository get() {
    return newInstance(contextProvider.get(), apiServiceProvider.get());
  }

  public static UserDataRepository_Factory create(Provider<Context> contextProvider,
      Provider<ApiService> apiServiceProvider) {
    return new UserDataRepository_Factory(contextProvider, apiServiceProvider);
  }

  public static UserDataRepository newInstance(Context context, ApiService apiService) {
    return new UserDataRepository(context, apiService);
  }
}
