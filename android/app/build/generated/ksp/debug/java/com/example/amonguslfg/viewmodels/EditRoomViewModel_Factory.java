package com.example.amonguslfg.viewmodels;

import androidx.lifecycle.SavedStateHandle;
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
public final class EditRoomViewModel_Factory implements Factory<EditRoomViewModel> {
  private final Provider<RoomRepository> roomRepositoryProvider;

  private final Provider<UserDataRepository> userDataRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public EditRoomViewModel_Factory(Provider<RoomRepository> roomRepositoryProvider,
      Provider<UserDataRepository> userDataRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.roomRepositoryProvider = roomRepositoryProvider;
    this.userDataRepositoryProvider = userDataRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public EditRoomViewModel get() {
    return newInstance(roomRepositoryProvider.get(), userDataRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static EditRoomViewModel_Factory create(Provider<RoomRepository> roomRepositoryProvider,
      Provider<UserDataRepository> userDataRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new EditRoomViewModel_Factory(roomRepositoryProvider, userDataRepositoryProvider, savedStateHandleProvider);
  }

  public static EditRoomViewModel newInstance(RoomRepository roomRepository,
      UserDataRepository userDataRepository, SavedStateHandle savedStateHandle) {
    return new EditRoomViewModel(roomRepository, userDataRepository, savedStateHandle);
  }
}
