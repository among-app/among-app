package com.example.amonguslfg.di;

import android.content.Context;
import com.example.amonguslfg.api.ApiService;
import com.example.amonguslfg.data.repository.UserDataRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideUserDataRepositoryFactory implements Factory<UserDataRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<ApiService> apiServiceProvider;

  public AppModule_ProvideUserDataRepositoryFactory(Provider<Context> contextProvider,
      Provider<ApiService> apiServiceProvider) {
    this.contextProvider = contextProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public UserDataRepository get() {
    return provideUserDataRepository(contextProvider.get(), apiServiceProvider.get());
  }

  public static AppModule_ProvideUserDataRepositoryFactory create(Provider<Context> contextProvider,
      Provider<ApiService> apiServiceProvider) {
    return new AppModule_ProvideUserDataRepositoryFactory(contextProvider, apiServiceProvider);
  }

  public static UserDataRepository provideUserDataRepository(Context context,
      ApiService apiService) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideUserDataRepository(context, apiService));
  }
}
