package com.example.amonguslfg.viewmodels;

import com.example.amonguslfg.data.repository.UserDataRepository;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UserDataRepository> userDataRepositoryProvider;

  public SettingsViewModel_Factory(Provider<UserDataRepository> userDataRepositoryProvider) {
    this.userDataRepositoryProvider = userDataRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(userDataRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<UserDataRepository> userDataRepositoryProvider) {
    return new SettingsViewModel_Factory(userDataRepositoryProvider);
  }

  public static SettingsViewModel newInstance(UserDataRepository userDataRepository) {
    return new SettingsViewModel(userDataRepository);
  }
}
