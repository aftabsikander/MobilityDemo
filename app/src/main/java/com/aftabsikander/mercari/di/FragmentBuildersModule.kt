package com.aftabsikander.mercari.di

import com.aftabsikander.mercari.ui.fragments.CategoryDetailFragment
import com.aftabsikander.mercari.ui.fragments.CategoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun contributeCategoryFragment(): CategoryFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCategoryDetailFragment(): CategoryDetailFragment

}