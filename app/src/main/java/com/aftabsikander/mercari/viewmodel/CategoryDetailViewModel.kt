package com.aftabsikander.mercari.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.aftabsikander.mercari.network.base.Resource
import com.aftabsikander.mercari.network.models.DisplayItem
import com.aftabsikander.mercari.network.repository.CategoryRepository
import javax.inject.Inject

class CategoryDetailViewModel
@Inject constructor(
    private var categoryRepository: CategoryRepository,
    application: Application
) : AndroidViewModel(application) {

    fun performCategoryLoad(categoryID: String): LiveData<PagedList<DisplayItem>> {
        return categoryRepository.loadCategoryData(categoryID)
    }

    fun getLiveDataForPaginationCallBack(): MutableLiveData<Resource<List<DisplayItem>>> {
        return categoryRepository.getDataLoadingForPaginationCallBacks()
    }
}