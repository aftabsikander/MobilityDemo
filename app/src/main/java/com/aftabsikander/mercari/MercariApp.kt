package com.aftabsikander.mercari

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.aftabsikander.mercari.di.AppComponent
import com.aftabsikander.mercari.di.AppInjector
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject

class MercariApp : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        context = this
        AppInjector.init(this)
        appComponent.inject(this)
        setupTimber()
        setupFabricForCrashlytics()

    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    companion object {
        private lateinit var context: MercariApp
        fun getInstance(): MercariApp {
            return context
        }
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    //region Helper methods for Fabric crashlytics
    private fun setupFabricForCrashlytics() {
        // Set up Crashlytics, disabled for debug builds
        val crashlyticsKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()

        Fabric.with(this, crashlyticsKit)
    }
    //endregion


}