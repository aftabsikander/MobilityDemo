package com.aftabsikander.mercari.di

import android.app.Application
import com.aftabsikander.mercari.MercariApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidInjectionModule::class), (AppModule::class), (MainActivityModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: MercariApp)

    fun getPerformanceChecker(): PerformanceChecker

    fun getOkHttpClient() : OkHttpClient
}