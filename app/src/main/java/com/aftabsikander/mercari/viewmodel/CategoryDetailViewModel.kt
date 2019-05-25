package com.aftabsikander.mercari.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import com.aftabsikander.mercari.network.models.DisplayItem
import com.aftabsikander.mercari.network.repository.CategoryRepository
import javax.inject.Inject

class CategoryDetailViewModel
@Inject constructor(
    private var categoryRepository: CategoryRepository,
    application: Application
) : AndroidViewModel(application) {

    private var liveDataCallbackForResource = MediatorLiveData<DisplayItem>()

    fun performCategoryLoad(categoryID: String): LiveData<PagedList<DisplayItem>> {
        liveDataCallbackForResource = categoryRepository.getDataLoadingForPaginationCallBacks()
        return categoryRepository.loadCategoryData(categoryID)
    }

    fun getResourceCallBack(): MediatorLiveData<DisplayItem> {
        return liveDataCallbackForResource
    }
}