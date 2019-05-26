package com.aftabsikander.mercari.di

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ImageLoadingModule {

    @Provides
    @Singleton
    fun provideActivityManager(application: Application): ActivityManager {
        return application.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    @Provides
    @Singleton
    fun getPerformanceChecker(activityManager: ActivityManager): PerformanceChecker {
        return PerformanceChecker(activityManager)
    }

}