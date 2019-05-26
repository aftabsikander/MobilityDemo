package com.aftabsikander.mercari.di

import com.aftabsikander.mercari.viewmodel.CategoryDetailViewModel
import com.aftabsikander.mercari.viewmodel.CategoryListViewModel
import dagger.Subcomponent

/**
 * A sub component to create ViewModels. It is called by the <class>
 * [com.aftabsikander.mercari.viewmodel.MercariViewModelFactory].
 */
@Subcomponent
interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelSubComponent
    }

    fun categoryListViewModel(): CategoryListViewModel

    fun categoryDetailViewModel(): CategoryDetailViewModel

}