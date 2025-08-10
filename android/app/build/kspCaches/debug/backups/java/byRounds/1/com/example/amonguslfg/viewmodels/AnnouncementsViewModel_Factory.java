package com.example.amonguslfg.viewmodels;

import com.example.amonguslfg.data.repository.AnnouncementRepository;
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
public final class AnnouncementsViewModel_Factory implements Factory<AnnouncementsViewModel> {
  private final Provider<AnnouncementRepository> repositoryProvider;

  public AnnouncementsViewModel_Factory(Provider<AnnouncementRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AnnouncementsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AnnouncementsViewModel_Factory create(
      Provider<AnnouncementRepository> repositoryProvider) {
    return new AnnouncementsViewModel_Factory(repositoryProvider);
  }

  public static AnnouncementsViewModel newInstance(AnnouncementRepository repository) {
    return new AnnouncementsViewModel(repository);
  }
}
