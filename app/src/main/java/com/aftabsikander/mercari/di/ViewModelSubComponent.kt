package com.aftabsikander.mercari.di

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

}