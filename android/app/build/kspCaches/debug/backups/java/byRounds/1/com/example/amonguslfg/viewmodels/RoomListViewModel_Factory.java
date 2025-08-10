package com.example.amonguslfg.viewmodels;

import com.example.amonguslfg.data.repository.RoomRepository;
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
public final class RoomListViewModel_Factory implements Factory<RoomListViewModel> {
  private final Provider<RoomRepository> repositoryProvider;

  private final Provider<UserDataRepository> userDataRepositoryProvider;

  public RoomListViewModel_Factory(Provider<RoomRepository> repositoryProvider,
      Provider<UserDataRepository> userDataRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.userDataRepositoryProvider = userDataRepositoryProvider;
  }

  @Override
  public RoomListViewModel get() {
    return newInstance(repositoryProvider.get(), userDataRepositoryProvider.get());
  }

  public static RoomListViewModel_Factory create(Provider<RoomRepository> repositoryProvider,
      Provider<UserDataRepository> userDataRepositoryProvider) {
    return new RoomListViewModel_Factory(repositoryProvider, userDataRepositoryProvider);
  }

  public static RoomListViewModel newInstance(RoomRepository repository,
      UserDataRepository userDataRepository) {
    return new RoomListViewModel(repository, userDataRepository);
  }
}
