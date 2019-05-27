package com.aftabsikander.mercari.di

import androidx.lifecycle.ViewModelProvider
import com.aftabsikander.mercari.viewmodel.base.MercariViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/***
 * App Module layer which contains dependency for our [androidx.lifecycle.AndroidViewModel],
 * [NetworkModule] layer, [DatabaseModule] layer, [ImageLoadingModule] layer.
 */
@Module(
    subcomponents = [(ViewModelSubComponent::class)],
    includes = [NetworkModule::class, DatabaseModule::class, ImageLoadingModule::class]
)
internal class AppModule {

    /**
     * Provide [ViewModelProvider.Factory] instance from our [ViewModelSubComponent] sub component layer.
     * @param viewModelSubComponent [ViewModelSubComponent] builder
     */
    @Singleton
    @Provides
    fun provideViewModelFactory(
        viewModelSubComponent: ViewModelSubComponent.Builder
    ): ViewModelProvider.Factory {
        return MercariViewModelFactory(viewModelSubComponent.build())
    }

}