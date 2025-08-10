package com.example.amonguslfg.viewmodels;

import com.example.amonguslfg.data.repository.AnnouncementRepository;
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
public final class AnnouncementManagerViewModel_Factory implements Factory<AnnouncementManagerViewModel> {
  private final Provider<AnnouncementRepository> repositoryProvider;

  private final Provider<UserDataRepository> userDataRepositoryProvider;

  public AnnouncementManagerViewModel_Factory(Provider<AnnouncementRepository> repositoryProvider,
      Provider<UserDataRepository> userDataRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.userDataRepositoryProvider = userDataRepositoryProvider;
  }

  @Override
  public AnnouncementManagerViewModel get() {
    return newInstance(repositoryProvider.get(), userDataRepositoryProvider.get());
  }

  public static AnnouncementManagerViewModel_Factory create(
      Provider<AnnouncementRepository> repositoryProvider,
      Provider<UserDataRepository> userDataRepositoryProvider) {
    return new AnnouncementManagerViewModel_Factory(repositoryProvider, userDataRepositoryProvider);
  }

  public static AnnouncementManagerViewModel newInstance(AnnouncementRepository repository,
      UserDataRepository userDataRepository) {
    return new AnnouncementManagerViewModel(repository, userDataRepository);
  }
}
