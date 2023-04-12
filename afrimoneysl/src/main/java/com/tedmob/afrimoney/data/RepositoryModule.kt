package com.tedmob.afrimoney.data

import com.tedmob.afrimoney.data.repository.PrefSessionRepository
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideSessionRepository(prefSessionRepository: PrefSessionRepository): SessionRepository
}