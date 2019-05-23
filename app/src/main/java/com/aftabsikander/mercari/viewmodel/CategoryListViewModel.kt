package com.aftabsikander.mercari.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aftabsikander.mercari.network.base.Resource
import com.aftabsikander.mercari.network.models.CategoryModel
import com.aftabsikander.mercari.network.repository.CategoryRepository
import javax.inject.Inject

class CategoryListViewModel
@Inject constructor(
    private var categoryRepository: CategoryRepository,
    application: Application
) : AndroidViewModel(application) {

    fun performStartLoad(): LiveData<Resource<List<CategoryModel>>> {
        return categoryRepository.loadCategories()
    }

}