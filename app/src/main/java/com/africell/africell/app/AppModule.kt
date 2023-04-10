package com.africell.africell.app

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    internal fun provideApp(application: Application): App = application as App

    @Provides
    @Singleton
    internal fun provideStringLoader(app: App): StringLoader = AndroidStringLoader(app)

    @Provides
    @Singleton
    internal fun provideExecutionSchedulers(): ExecutionSchedulers = DefaultSchedulers()

}