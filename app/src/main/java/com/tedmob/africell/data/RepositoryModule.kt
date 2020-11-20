package com.tedmob.africell.data

import com.tedmob.africell.data.repository.PrefSessionRepository
import com.tedmob.africell.data.repository.domain.SessionRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideSessionRepository(prefSessionRepository: PrefSessionRepository): SessionRepository
}