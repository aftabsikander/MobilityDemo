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
 * [T] Input Request Response
 * [V] The type of the entries in the list which would be passed back as [PagedList] source
 * [K] Output type for LiveData observable which would be listened on UI for Network Info
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


    fun setupPagination(): LiveData<PagedList<T>> {
        //Create and store realm source factor for data mapping
        realmSourceFactor = this.createRealmDataSource()
        dataSourceFactoryForPagingComponent = this.createDataSourceMapping(realmSourceFactor)
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

    private fun getCustomErrorMessage(error: Throwable): String {
        return when (error) {
            is SocketTimeoutException -> MercariApp.getInstance().getString(R.string.requestTimeOutError)
            is MalformedJsonException -> MercariApp.getInstance().getString(R.string.responseMalformedJson)
            is IOException -> MercariApp.getInstance().getString(R.string.networkError)
            is HttpException -> error.response().message()
            else -> MercariApp.getInstance().getString(R.string.unknownError)
        }
    }

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

    @WorkerThread
    protected abstract fun saveCallResult(item: V?)

    @MainThread
    private fun shouldFetch(): Boolean {
        return true
    }

    //endregion

    //region abstract helper methods
    @MainThread
    protected abstract fun loadFromDb(): MutableLiveData<K>

    @MainThread
    protected abstract fun provideMonarchyInstance(): Monarchy

    @MainThread
    protected abstract fun createRealmDataSource(): Monarchy.RealmDataSourceFactory<T>

    @MainThread
    protected abstract fun createDataSourceMapping(realmDataSource: Monarchy.RealmDataSourceFactory<T>): DataSource.Factory<Int, T>

    @MainThread
    protected abstract fun createCall(): Call<V>?

    @MainThread
    protected abstract fun providePageConfig(): PagedList.Config

    protected abstract fun provideInitialLoadKey(): Int

    protected abstract fun liveDataReceiver(receiver: MutableLiveData<Resource<K>>)

    //endregion

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
