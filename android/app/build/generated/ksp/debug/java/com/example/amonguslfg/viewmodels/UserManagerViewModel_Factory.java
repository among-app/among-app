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
  private final Provider<UserDataRepository> repositoryProvider;

  public UserManagerViewModel_Factory(Provider<UserDataRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UserManagerViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static UserManagerViewModel_Factory create(
      Provider<UserDataRepository> repositoryProvider) {
    return new UserManagerViewModel_Factory(repositoryProvider);
  }

  public static UserManagerViewModel newInstance(UserDataRepository repository) {
    return new UserManagerViewModel(repository);
  }
}
