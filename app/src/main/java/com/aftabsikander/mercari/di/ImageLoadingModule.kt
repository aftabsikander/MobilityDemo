package com.aftabsikander.mercari.di

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Image Loading layer which contains key component providers.
 */
@Module
class ImageLoadingModule {

    /**
     * Provides [ActivityManager] instance which will be used by [PerformanceChecker] class for performance monitoring.
     * @param application [Application] instance for getting instance of [ActivityManager]
     *
     * @return  [ActivityManager] instance
     */
    @Provides
    @Singleton
    fun provideActivityManager(application: Application): ActivityManager {
        return application.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    /**
     * Provides [PerformanceChecker] instance which will be used for Image loading performance monitoring by
     * [com.aftabsikander.mercari.glide.ImageGlideModule] class.
     * @param activityManager [ActivityManager] instance
     *
     * @return  [PerformanceChecker] instance
     */
    @Provides
    @Singleton
    fun getPerformanceChecker(activityManager: ActivityManager): PerformanceChecker {
        return PerformanceChecker(activityManager)
    }

}