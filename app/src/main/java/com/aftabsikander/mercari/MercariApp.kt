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

/**
 * Application class which setup dagger injection and crash reporting tool.
 * Included [MultiDexApplication] support for further development.
 *
 * @see [HasActivityInjector]
 */
class MercariApp : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var appComponent: AppComponent

    /**
     * Singleton object for [MercariApp] for future references
     */
    companion object {
        private lateinit var context: MercariApp
        fun getInstance(): MercariApp {
            return context
        }
    }

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


    //region Setup Logging Client
    /**
     * Setup Logging client which we would be using in our application
     *
     * @see[Timber]
     */
    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
    //endregion

    //region Setup Fabric crashlytics

    /**
     * Setup Crash reporting tool i.e [Crashlytics] We will only send release crashes to it's dashboard,
     * all [BuildConfig.DEBUG] crashes will not be pushed.
     *
     * @see [Fabric]
     */
    private fun setupFabricForCrashlytics() {
        val crashlyticsKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()

        Fabric.with(this, crashlyticsKit)
    }
    //endregion


}