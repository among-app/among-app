package com.example.amonguslfg.services;

import com.example.amonguslfg.data.repository.UserDataRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MyFirebaseMessagingService_MembersInjector implements MembersInjector<MyFirebaseMessagingService> {
  private final Provider<UserDataRepository> userDataRepositoryProvider;

  public MyFirebaseMessagingService_MembersInjector(
      Provider<UserDataRepository> userDataRepositoryProvider) {
    this.userDataRepositoryProvider = userDataRepositoryProvider;
  }

  public static MembersInjector<MyFirebaseMessagingService> create(
      Provider<UserDataRepository> userDataRepositoryProvider) {
    return new MyFirebaseMessagingService_MembersInjector(userDataRepositoryProvider);
  }

  @Override
  public void injectMembers(MyFirebaseMessagingService instance) {
    injectUserDataRepository(instance, userDataRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.example.amonguslfg.services.MyFirebaseMessagingService.userDataRepository")
  public static void injectUserDataRepository(MyFirebaseMessagingService instance,
      UserDataRepository userDataRepository) {
    instance.userDataRepository = userDataRepository;
  }
}
