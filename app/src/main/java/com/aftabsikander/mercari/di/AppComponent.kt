package com.aftabsikander.mercari.di

import android.app.Application
import com.aftabsikander.mercari.MercariApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Application main component layer which holder all the dependencies and later would be used across the application layer.
 *
 * <p> It contains [AndroidInjectionModule] module, [AppModule] module which contains layer like network,database etc,
 *  and most importantly our activity module [MainActivityModule] which will hold all the reference of our activities.
 * </p>
 */
@Singleton
@Component(modules = [(AndroidInjectionModule::class), (AppModule::class), (MainActivityModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    /**
     * Inject our application object [MercariApp] in dependency graph for future reference.
     * @param app [MercariApp] application object instance
     */
    fun inject(app: MercariApp)

    /**
     * Retrieve [PerformanceChecker] instance which hold information about device
     *
     * @return [PerformanceChecker] instance
     */
    fun getPerformanceChecker(): PerformanceChecker

    /**
     * Retrieve [OkHttpClient] instance with our pre configuration
     *
     * @return [OkHttpClient] instance
     */
    fun getOkHttpClient(): OkHttpClient
}