package com.aftabsikander.mercari.network.repository

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.aftabsikander.mercari.network.base.NetworkBoundResource
import com.aftabsikander.mercari.network.base.NetworkPaginatedBoundResource
import com.aftabsikander.mercari.network.base.Resource
import com.aftabsikander.mercari.network.models.CategoryModel
import com.aftabsikander.mercari.network.models.DisplayItem
import com.aftabsikander.mercari.network.services.MercariService
import com.aftabsikander.mercari.utilities.constants.AppConstants
import com.zhuinden.monarchy.Monarchy
import io.realm.Case
import io.realm.Realm
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class for [CategoryModel] and [DisplayItem] data source which holds all the configuration.
 * This class is responsible for loading data and passing it as observable which can be used by anyone.
 *
 * @param service [MercariService] service instance.
 * @param monarchy [Monarchy] instance for [io.realm.Realm] related operations.
 */
@Singleton
class CategoryRepository @Inject
constructor(private val service: MercariService, var monarchy: Monarchy) {

    private lateinit var liveDataReceiverForPagination: MutableLiveData<Resource<List<DisplayItem>>>


    /**
     * Load categories data from network and cache it for further usage for offline.
     * [NetworkBoundResource] helper class is being used for encapsulating complex operations of fetching data
     * from server or loading from cache.
     *
     * @return [LiveData] instance which contains [Resource] object holding [CategoryModel] collections.
     */
    fun loadCategories(): LiveData<Resource<List<CategoryModel>>> {
        return object : NetworkBoundResource<List<CategoryModel>, ArrayList<CategoryModel>>() {
            override fun saveCallResult(item: ArrayList<CategoryModel>?) {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    if (!item.isNullOrEmpty()) {
                        it.insertOrUpdate(item)
                    }
                }
                realm.close()
            }

            @NonNull
            override fun loadFromDb(): LiveData<List<CategoryModel>> {
                return monarchy.findAllCopiedWithChanges { realm -> realm.where(CategoryModel::class.java) }
            }

            @NonNull
            override fun createCall(): Call<ArrayList<CategoryModel>> {
                return service.getStartupData()
            }
        }.asLiveData
    }

    /**
     * Loads [DisplayItem] data from network and cache it for further usage for offline.
     * [NetworkPaginatedBoundResource] helper class is being used for encapsulating complex operations of fetching data
     * from server or loading from cache. It provides data as pagination for our [androidx.paging] library.
     * @param categoryID Category ID
     *
     * @return [LiveData] instance which contains [PagedList] object holding [DisplayItem] collections.
     */
    fun loadCategoryData(categoryID: String): LiveData<PagedList<DisplayItem>> {
        return object : NetworkPaginatedBoundResource<DisplayItem, ArrayList<DisplayItem>, List<DisplayItem>>() {

            override fun saveCallResult(item: ArrayList<DisplayItem>?) {
                val realmInstance = Realm.getDefaultInstance()
                realmInstance.executeTransaction { realm ->
                    if (item != null) {
                        item.forEach {
                            it.categoryName = categoryID
                            //this is important for generating unique PrimaryKey
                            it.generateUniqueID()
                        }
                        realm.insertOrUpdate(item)
                    }
                }
                realmInstance.close()
            }

            override fun loadFromDb(): MutableLiveData<List<DisplayItem>> {
                return monarchy.findAllCopiedWithChanges { realm ->
                    realm.where(DisplayItem::class.java).equalTo(
                        "categoryName", categoryID, Case.INSENSITIVE
                    )
                } as MutableLiveData<List<DisplayItem>>
            }

            override fun provideMonarchyInstance(): Monarchy {
                return monarchy
            }

            override fun createRealmDataSource(): Monarchy.RealmDataSourceFactory<DisplayItem> {
                return monarchy.createDataSourceFactory { realm ->
                    realm.where(DisplayItem::class.java).equalTo(
                        "categoryName", categoryID, Case.INSENSITIVE
                    )
                } as Monarchy.RealmDataSourceFactory<DisplayItem>
            }

            override fun createDataSourceMapping(realmDataSource: Monarchy.RealmDataSourceFactory<DisplayItem>):
                    DataSource.Factory<Int, DisplayItem> {
                return realmDataSource.map { input ->
                    DisplayItem(
                        input.uniqueID, input.id, input.categoryName, input.status, input.name, input.likeCount,
                        input.commentCounts, input.amount, input.imgURL
                    )
                }
            }

            override fun createCall(): Call<ArrayList<DisplayItem>>? {
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

            override fun liveDataReceiver(receiver: MutableLiveData<Resource<List<DisplayItem>>>) {
                liveDataReceiverForPagination = receiver
            }
        }.setupPagination()
    }


    /**
     * Retrieve Pagination network callback observer which will be used on UI for further usage.
     *
     * @return [MutableLiveData] instance which contains [Resource] object holding [DisplayItem] collections.
     */
    fun getDataLoadingForPaginationCallBacks(): MutableLiveData<Resource<List<DisplayItem>>> {
        return liveDataReceiverForPagination
    }

    /**
     * Extract Display Item URL from [CategoryModel] which will be used when fetching data from network.
     * @param categoryID Category ID
     *
     * @return [Call] instance for [retrofit2.Retrofit] network call.
     */
    private fun generateCategoryDataEndPoint(categoryID: String): Call<ArrayList<DisplayItem>> {
        var categoryURL = ""
        val realmInstance = Realm.getDefaultInstance()
        realmInstance.executeTransaction {
            val category = it.where(CategoryModel::class.java)
                .equalTo("name", categoryID, Case.INSENSITIVE)
                .findFirst()
            categoryURL = category?.dataURL ?: ""
        }
        realmInstance.close()
        return service.getCategoryData(categoryURL)
    }


}
