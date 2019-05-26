package com.aftabsikander.mercari.viewmodel.base

import androidx.collection.ArrayMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aftabsikander.mercari.di.ViewModelSubComponent
import com.aftabsikander.mercari.viewmodel.CategoryDetailViewModel
import com.aftabsikander.mercari.viewmodel.CategoryListViewModel
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MercariViewModelFactory @Inject
constructor(viewModelSubComponent: ViewModelSubComponent) : ViewModelProvider.Factory {
    private val creators: ArrayMap<Class<*>, Callable<out ViewModel>> = ArrayMap()

    init {
        // View models cannot be injected directly because they won't be bound to the owner's view model scope.
        creators[CategoryListViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.categoryListViewModel() }

        creators[CategoryDetailViewModel::class.java] =
            Callable<ViewModel> { viewModelSubComponent.categoryDetailViewModel() }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        var creator: Callable<out ViewModel>? = creators[modelClass]

        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }

        if (creator == null) {
            throw IllegalArgumentException("Unknown model class $modelClass")
        }

        try {
            return creator.call() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}