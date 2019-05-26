package com.aftabsikander.mercari.di

import android.app.ActivityManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper class for checking device available processors and memory class for making decision for
 * [com.aftabsikander.mercari.glide.ImageGlideModule] class Memory Category selection.

 * @param activityManager [ActivityManager] instance
 */
@Singleton
class PerformanceChecker @Inject constructor(activityManager: ActivityManager) {

    private val isHighPerformingDevice: Boolean

    init {
        isHighPerformingDevice =
            !activityManager.isLowRamDevice &&
                    Runtime.getRuntime().availableProcessors() >= OPTIMUM_CORE &&
                    activityManager.memoryClass >= OPTIMUM_MEMORY_MB
    }

    companion object {
        private const val OPTIMUM_CORE = 4
        private const val OPTIMUM_MEMORY_MB = 124
    }

    /**
     * Retrieve device performance result
     *
     * @return True if device is high performance device false is its low memory device.
     */
    fun getDevicePerformanceResult(): Boolean {
        return isHighPerformingDevice
    }
}