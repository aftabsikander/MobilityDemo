package com.aftabsikander.mercari.di

import android.app.ActivityManager
import javax.inject.Inject
import javax.inject.Singleton

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

    fun getDevicePerformanceResult(): Boolean {
        return isHighPerformingDevice
    }
}