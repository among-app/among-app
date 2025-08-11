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
public final class UserManagerViewModel_Factory implements Factory<UserManagerViewModel> {
  private final Provider<UserDataRepository> userDataRepositoryProvider;

  public UserManagerViewModel_Factory(Provider<UserDataRepository> userDataRepositoryProvider) {
    this.userDataRepositoryProvider = userDataRepositoryProvider;
  }

  @Override
  public UserManagerViewModel get() {
    return newInstance(userDataRepositoryProvider.get());
  }

  public static UserManagerViewModel_Factory create(
      Provider<UserDataRepository> userDataRepositoryProvider) {
    return new UserManagerViewModel_Factory(userDataRepositoryProvider);
  }

  public static UserManagerViewModel newInstance(UserDataRepository userDataRepository) {
    return new UserManagerViewModel(userDataRepository);
  }
}
