package com.aftabsikander.mercari.network.repository

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.aftabsikander.mercari.network.base.NetworkBoundResource
import com.aftabsikander.mercari.network.base.NetworkPaginatedBoundResource
import com.aftabsikander.mercari.network.base.Resource
import com.aftabsikander.mercari.network.models.CategoryModel
import com.aftabsikander.mercari.network.models.CategoryResponse
import com.aftabsikander.mercari.network.models.DisplayItem
import com.aftabsikander.mercari.network.models.StartupResponse
import com.aftabsikander.mercari.network.services.MercariService
import com.aftabsikander.mercari.utilities.AppConstants
import com.zhuinden.monarchy.Monarchy
import io.realm.Case
import io.realm.Realm
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CategoryRepository @Inject
constructor(private val service: MercariService, private val realm: Realm, var monarchy: Monarchy) {

    private val liveDataCallbackForResource = MediatorLiveData<DisplayItem>()

    fun loadCategories(): LiveData<Resource<List<CategoryModel>>> {
        return object : NetworkBoundResource<List<CategoryModel>, StartupResponse>() {
            override fun saveCallResult(item: StartupResponse?) {
                realm.executeTransaction {
                    if (item != null) {
                        it.insertOrUpdate(item.catTabs)
                    }
                }
            }

            @NonNull
            override fun loadFromDb(): LiveData<List<CategoryModel>> {
                return monarchy.findAllCopiedWithChanges { realm -> realm.where(CategoryModel::class.java) }
            }

            @NonNull
            override fun createCall(): Call<StartupResponse> {
                return service.getStartupData()
            }
        }.asLiveData
    }


    fun loadCategoryData(categoryID: String): LiveData<PagedList<DisplayItem>> {
        return object : NetworkPaginatedBoundResource<DisplayItem, CategoryResponse>() {
            override fun saveCallResult(item: CategoryResponse?) {
                realm.executeTransaction { realm ->
                    if (item != null) {
                        item.catDataCol.forEach {
                            it.categoryID = categoryID
                        }
                        realm.insertOrUpdate(item.catDataCol)
                    }
                }
            }

            override fun loadFromDb(): LiveData<DisplayItem> {
                return liveDataCallbackForResource
            }

            override fun provideMonarchyInstance(): Monarchy {
                return monarchy
            }

            override fun createRealmDataSource(): Monarchy.RealmDataSourceFactory<DisplayItem> {
                return monarchy.createDataSourceFactory { realm ->
                    realm.where(DisplayItem::class.java).equalTo(
                        "categoryID", categoryID, Case.INSENSITIVE
                    )
                } as Monarchy.RealmDataSourceFactory<DisplayItem>
            }

            override fun createDataSourceMapping(realmDataSource: Monarchy.RealmDataSourceFactory<DisplayItem>):
                    DataSource.Factory<Int, DisplayItem> {
                return realmDataSource.map { input ->
                    DisplayItem(
                        input.id, input.categoryID, input.status, input.name, input.likeCount,
                        input.commentCounts, input.amount, input.imgURL
                    )
                }
            }

            override fun createCall(): Call<CategoryResponse>? {
                return generateCategoryDataEndPoint(categoryID)
            }

            override fun providePageConfig(): PagedList.Config {
                return PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(AppConstants.INITIAL_LOAD_SIZE_HINT)
                    .setPrefetchDistance(AppConstants.PRE_FETCH_DISTANCE)
                    .setPageSize(AppConstants.RECORD_LIMIT)
                    .build()
            }

            override fun provideInitialLoadKey(): Int {
                return AppConstants.INITIAL_PAGE
            }

        }.setupPagination()
    }

    fun getDataLoadingForPaginationCallBacks(): MediatorLiveData<DisplayItem> {
        return liveDataCallbackForResource
    }

    private fun generateCategoryDataEndPoint(categoryID: String): Call<CategoryResponse> {
        var categoryURL = ""
        realm.executeTransaction {
            val category = it.where(CategoryModel::class.java)
                .equalTo("name", categoryID, Case.INSENSITIVE)
                .findFirst()
            categoryURL = category?.dataURL ?: ""
        }
        return service.getCategoryData(categoryURL)
    }


}
