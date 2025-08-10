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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<UserDataRepository> userDataRepositoryProvider;

  public MainViewModel_Factory(Provider<UserDataRepository> userDataRepositoryProvider) {
    this.userDataRepositoryProvider = userDataRepositoryProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(userDataRepositoryProvider.get());
  }

  public static MainViewModel_Factory create(
      Provider<UserDataRepository> userDataRepositoryProvider) {
    return new MainViewModel_Factory(userDataRepositoryProvider);
  }

  public static MainViewModel newInstance(UserDataRepository userDataRepository) {
    return new MainViewModel(userDataRepository);
  }
}
