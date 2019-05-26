package com.aftabsikander.mercari.network.base

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aftabsikander.mercari.MercariApp
import com.aftabsikander.mercari.R
import com.google.gson.stream.MalformedJsonException
import com.zhuinden.monarchy.Monarchy
import io.realm.RealmModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * This class act as the decider to cache the response/ fetch from the service always as a paginated data source.
 * @param [T] Instance of [RealmModel] Input Request Response
 * @param [V] The type of the entries in the list which would be passed back as [PagedList] source
 * @param [K] Output type for LiveData observable which would be listened on UI for Network Info
 *
 * @see [RealmModel]
 * @see [PagedList]
 * @see [Resource]
 */
abstract class NetworkPaginatedBoundResource<T : RealmModel, V, K> @MainThread
protected constructor() {

    private lateinit var realmSourceFactor: Monarchy.RealmDataSourceFactory<T>
    private lateinit var dataSourceFactoryForPagingComponent: DataSource.Factory<Int, T>
    private lateinit var paginationCallback: BoundaryPaginationCallback
    private var dataSourceLive: MutableLiveData<K>
    private var monarchy: Monarchy
    private val result = MediatorLiveData<Resource<K>>()
    val asLiveData: MutableLiveData<Resource<K>> get() = result


    /**
     * Set data source as pagination for our [androidx.paging] library, generate observable [LiveData]
     * object which can be observer by anyone.
     */
    fun setupPagination(): LiveData<PagedList<T>> {

        //Create and store realm source factor for data mapping
        realmSourceFactor = this.createRealmDataSource()
        dataSourceFactoryForPagingComponent = this.createDataSourceMapping(realmSourceFactor)

        // create Boundary callbacks for hitting Network calls when data source list has reached from database source.
        paginationCallback = BoundaryPaginationCallback(this)


        return monarchy.findAllPagedWithChanges(
            realmSourceFactor,
            LivePagedListBuilder<Int, T>(dataSourceFactoryForPagingComponent, providePageConfig())
                .setInitialLoadKey(provideInitialLoadKey())
                .setBoundaryCallback(paginationCallback)
        ) as LiveData<PagedList<T>>
    }

    init {
        this.liveDataReceiver(result)
        monarchy = this.provideMonarchyInstance()
        dataSourceLive = this.loadFromDb()
        // Fetch the data from network and add it to the resource
        result.addSource(dataSourceLive) {
            result.removeSource(dataSourceLive)
            if (shouldFetch()) {
                fetchFromNetwork(dataSourceLive)
            } else {
                result.addSource(dataSourceLive) { newData ->
                    if (null != newData)
                        result.value = Resource.success(newData)
                }
            }
        }

    }


    //region Helper methods
    /**
     * This method fetches the data from remote service and save it to local db
     *
     * @param [dbSource] - Database source
     */
    private fun fetchFromNetwork(dbSource: LiveData<K>) {
        result.value = Resource.loading(true)
        createCall()?.enqueue(object : Callback<V> {
            override fun onResponse(call: Call<V>, response: Response<V>) {
                result.removeSource(dbSource)
                saveResultAndReInit(response.body())
            }

            override fun onFailure(call: Call<V>, t: Throwable) {
                Timber.d(t)
                result.removeSource(dbSource)
                loadOfflineData(t)
            }
        })
    }

    /**
     * Provide Generic error message for Network errors.
     * @param error [Throwable] object on which error message would be generated.
     *
     * @return Custom error messages for network failure cases.
     *
     * @see [SocketTimeoutException]
     * @see [MalformedJsonException]
     * @see [IOException]
     * @see [HttpException]
     *
     */
    private fun getCustomErrorMessage(error: Throwable): String {
        return when (error) {
            is SocketTimeoutException -> MercariApp.getInstance().getString(R.string.requestTimeOutError)
            is MalformedJsonException -> MercariApp.getInstance().getString(R.string.responseMalformedJson)
            is IOException -> MercariApp.getInstance().getString(R.string.networkError)
            is HttpException -> error.response().message()
            else -> MercariApp.getInstance().getString(R.string.unknownError)
        }
    }

    /***
     * Pass result to Main thread when successful Network call is made and send result back to our main observable
     *
     * @param response [V] data response received from network call.
     */
    @MainThread
    private fun saveResultAndReInit(response: V?) {
        doAsync {
            saveCallResult(response)
            uiThread {
                result.addSource(loadFromDb()) { newData ->
                    if (null != newData)
                        result.value = Resource.success(newData)
                }
            }
        }
    }

    /**
     * Should network API be called for fetching latest data.
     * @return True as we would be calling API always
     */
    @MainThread
    private fun shouldFetch(): Boolean {
        return true
    }

    //endregion

    //region abstract helper methods

    /**
     * Saving of network response data in our persistent storage.
     * @param item [V] data received from network response.
     */
    @WorkerThread
    protected abstract fun saveCallResult(item: V?)

    /**
     * Loading of data from DB layer as Observable database source
     *
     * @return [androidx.lifecycle.MutableLiveData] data source as observable having [K] data
     */
    @MainThread
    protected abstract fun loadFromDb(): MutableLiveData<K>

    /**
     * Provide [Monarchy] Instance on which [androidx.paging.PagedList] would be created.
     *
     * @return [Monarchy] Instance
     */
    @MainThread
    protected abstract fun provideMonarchyInstance(): Monarchy

    /**
     * Creating [Monarchy.RealmDataSourceFactory] database which would be used to fetch data from [io.realm.Realm]
     *
     * @return [Monarchy.RealmDataSourceFactory] instance having [T] data
     */
    @MainThread
    protected abstract fun createRealmDataSource(): Monarchy.RealmDataSourceFactory<T>

    /**
     * Creating Data source mapping collection for [androidx.paging.DataSource.Factory] which will be used by [androidx.paging]
     * which takes [Int] as Key for mapping and [T] for the type of items in the list loaded by the DataSources.
     *
     * @return [DataSource.Factory]
     */
    @MainThread
    protected abstract fun createDataSourceMapping(realmDataSource: Monarchy.RealmDataSourceFactory<T>): DataSource.Factory<Int, T>

    /**
     * Creates [retrofit2.Call] instance which would be used by [retrofit2.Call] for calling network request.
     *
     * @return [retrofit2.Call]] instance having [V] data source
     */
    @MainThread
    protected abstract fun createCall(): Call<V>?

    /**
     * Provide [androidx.paging.PagedList.Config] instance which will be used by
     * [androidx.paging.LivePagedListBuilder] for generating paging result
     *
     * @return [androidx.paging.PagedList.Config] instance.
     */
    @MainThread
    protected abstract fun providePageConfig(): PagedList.Config

    /**
     * Provide Initial Pagination Key for calling paging API from network layer. This key would be used by
     *  [androidx.paging.LivePagedListBuilder] for pagination
     *
     *  @return [Int] key for pagination API initial payload.
     */
    @MainThread
    protected abstract fun provideInitialLoadKey(): Int

    /**
     * Network layer observable which UI can observe to for network changes.
     * @param receiver [androidx.lifecycle.MediatorLiveData] instance
     */
    @MainThread
    protected abstract fun liveDataReceiver(receiver: MutableLiveData<Resource<K>>)

    //endregion

    /**
     * Provide offline cached data when we receive any error from network layer,
     * If cache layer is empty we will pass network error back to UI.
     *
     * @param t [Throwable] instance for parsing custom error messages.
     */
    private fun loadOfflineData(t: Throwable) {
        result.addSource(loadFromDb()) { newData ->
            if (null != newData) {
                result.value = Resource.success(newData)
            } else {
                result.setValue(
                    Resource.error(
                        getCustomErrorMessage(t),
                        newData
                    )
                )
            }
        }
    }


    /**
     * Pagination Network callback which fetches data from server when end of the collection is hit on our cache storage.
     *
     */
    private inner class BoundaryPaginationCallback(
        private val networkBoundResource: NetworkPaginatedBoundResource<T, V, K>
    ) : PagedList.BoundaryCallback<T>() {

        /**
         * Database returned 0 items. We should query the backend for more items.
         */
        override fun onZeroItemsLoaded() {
            networkBoundResource.fetchFromNetwork(networkBoundResource.dataSourceLive)
        }

        /**
         * When all items in the database were loaded, we need to query the backend for more items.
         */
        override fun onItemAtEndLoaded(itemAtEnd: T) {
            //TODO will be implemented later when pagination API is available.
        }
    }

}
