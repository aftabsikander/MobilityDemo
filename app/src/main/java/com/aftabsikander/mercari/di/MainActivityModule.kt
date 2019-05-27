package com.aftabsikander.mercari.di

import com.aftabsikander.mercari.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * All [androidx.appcompat.app.AppCompatActivity] Dependency graph for [dagger.android] injection
 */
@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    internal abstract fun contributeMainActivity(): MainActivity

}