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

/**
 * [androidx.lifecycle.ViewModel] instance which holds all the helper methods for UI interaction for
 * [com.aftabsikander.mercari.ui.fragments.CategoryDetailFragment] fragment
 *
 * @see [com.aftabsikander.mercari.ui.fragments.CategoryDetailFragment]
 */
class CategoryDetailViewModel

/**
 * Constructor which setup our [CategoryDetailViewModel] for further usage.
 *
 * @param categoryRepository Repository [CategoryRepository] instance which will provide data for displaying on UI.
 * @param application [Application] instance for passing it to [AndroidViewModel] constructor.
 */
@Inject constructor(
    private var categoryRepository: CategoryRepository,
    application: Application
) : AndroidViewModel(application) {

    /**
     * Loads display items from repository and passes the result back to fragment.
     * Data is passed via live data observable having [PagedList]
     * data as we would be showing it as paginated collection
     *
     * @param [categoryID] category ID whose data needs to be displayed.
     * @return Paginated collection via Live data instance using [PagedList] collection
     *
     * @see [PagedList]
     * @see [DisplayItem]
     * @see [LiveData]
     */
    fun performDisplayItemLoading(categoryID: String): LiveData<PagedList<DisplayItem>> {
        return categoryRepository.loadCategoryData(categoryID)
    }

    /**
     * Pagination callback listener which is used for listening to network operations from repository
     * result is being passed back to UI
     *
     * @return MutableLiveData observable having [Resource]
     * @see [Resource]
     * @see [DisplayItem]
     *
     */
    fun getLiveDataForPaginationCallBack(): MutableLiveData<Resource<List<DisplayItem>>> {
        return categoryRepository.getDataLoadingForPaginationCallBacks()
    }
}