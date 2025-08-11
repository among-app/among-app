package com.example.amonguslfg.viewmodels;

import androidx.lifecycle.SavedStateHandle;
import com.example.amonguslfg.data.repository.RoomRepository;
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
public final class RoomDetailViewModel_Factory implements Factory<RoomDetailViewModel> {
  private final Provider<RoomRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public RoomDetailViewModel_Factory(Provider<RoomRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public RoomDetailViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static RoomDetailViewModel_Factory create(Provider<RoomRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new RoomDetailViewModel_Factory(repositoryProvider, savedStateHandleProvider);
  }

  public static RoomDetailViewModel newInstance(RoomRepository repository,
      SavedStateHandle savedStateHandle) {
    return new RoomDetailViewModel(repository, savedStateHandle);
  }
}
