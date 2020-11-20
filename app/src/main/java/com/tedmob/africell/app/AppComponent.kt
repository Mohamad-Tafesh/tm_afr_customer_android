package com.tedmob.africell.app

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AndroidSupportInjectionModule::class, InjectorsModule::class])
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    abstract class Factory: AndroidInjector.Factory<App>
}