package com.tedmob.africell.app

import android.app.Application
import android.content.Context
import com.tedmob.africell.data.DataModule
import com.tedmob.africell.data.RepositoryModule
import com.tedmob.africell.ui.viewmodel.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class, ViewModelModule::class, DataModule::class])
object AppModule {

    @Provides
    @Singleton
    internal fun provideApplication(app: App): Application = app

    @Provides
    @Singleton
    internal fun provideContext(app: App): Context = app

    @Provides
    @Singleton
    internal fun provideStringLoader(app: App): StringLoader = AndroidStringLoader(app)

    @Provides
    @Singleton
    internal fun provideExecutionSchedulers(): ExecutionSchedulers = DefaultSchedulers()

}