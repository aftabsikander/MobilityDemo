package com.aftabsikander.mercari.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aftabsikander.mercari.network.base.Resource
import com.aftabsikander.mercari.network.models.CategoryModel
import com.aftabsikander.mercari.network.repository.CategoryRepository
import javax.inject.Inject

/**
 * [androidx.lifecycle.ViewModel] instance which holds all the helper methods for UI interaction for
 * [com.aftabsikander.mercari.ui.fragments.CategoryFragment] fragment.
 *
 * @see [com.aftabsikander.mercari.ui.fragments.CategoryFragment]
 * @see [CategoryRepository]
 */
class CategoryListViewModel

/**
 * Constructor which setup our [CategoryListViewModel] for further usage.
 *
 * @param categoryRepository Repository [CategoryRepository] instance which will provide data for displaying on UI.
 * @param application [Application] instance for passing it to [AndroidViewModel] constructor.
 */
@Inject constructor(
    private var categoryRepository: CategoryRepository,
    application: Application
) : AndroidViewModel(application) {


    /**
     * Loads [CategoryModel] data from repository instance and pass data back to UI.
     * @return Live data observable containing [Resource] object which holds [CategoryModel] collection.
     *
     * @see [LiveData]
     * @see [Resource]
     * @see [CategoryModel]
     */
    fun loadCategoryData(): LiveData<Resource<List<CategoryModel>>> {
        return categoryRepository.loadCategories()
    }

}