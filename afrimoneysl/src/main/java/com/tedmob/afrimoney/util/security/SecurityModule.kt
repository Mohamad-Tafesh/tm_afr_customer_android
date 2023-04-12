package com.tedmob.afrimoney.util.security

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    @Named("local-string")
    internal fun providesStringEncryptor(encryptor: AndroidStringEncryptor): StringEncryptor =
        encryptor.doNotHandleEmpty().safe()
}