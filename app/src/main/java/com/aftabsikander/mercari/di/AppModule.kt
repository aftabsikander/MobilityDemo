package com.aftabsikander.mercari.di

import androidx.lifecycle.ViewModelProvider
import com.aftabsikander.mercari.viewmodel.base.MercariViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(
    subcomponents = [(ViewModelSubComponent::class)],
    includes = [NetworkModule::class, DatabaseModule::class, ImageLoadingModule::class]
)
internal class AppModule {

    @Singleton
    @Provides
    fun provideViewModelFactory(
        viewModelSubComponent: ViewModelSubComponent.Builder
    ): ViewModelProvider.Factory {
        return MercariViewModelFactory(viewModelSubComponent.build())
    }

}