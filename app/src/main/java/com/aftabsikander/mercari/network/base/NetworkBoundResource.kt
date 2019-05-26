package com.aftabsikander.mercari.network.base

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.aftabsikander.mercari.MercariApp
import com.aftabsikander.mercari.R
import com.google.gson.stream.MalformedJsonException
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
 * This class act as the decider to cache the response/ fetch from the service always.
 * @param [T] Input Request Response
 * @param [V] The type of the entries in the list which would be passed back as source
 *
 * @see [Resource]
 */

abstract class NetworkBoundResource<T, V> @MainThread
protected constructor() {

    private val result = MediatorLiveData<Resource<T>>()

    val asLiveData: LiveData<Resource<T>> get() = result

    init {
        result.value = Resource.loading(true)

        // Always load the data from DB initially so that we have
        val dbSource = this.loadFromDb()

        // Fetch the data from network and add it to the resource
        result.addSource(dbSource) {
            result.removeSource(dbSource)
            if (shouldFetch()) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    if (null != newData)
                        result.value = Resource.success(newData)
                }
            }
        }
    }

    /**
     * This method fetches the data from remote service and save it to local db
     *
     * @param dbSource - Database source
     */
    private fun fetchFromNetwork(dbSource: LiveData<T>) {
        result.addSource(dbSource) {
            result.setValue(Resource.loading(true))
        }
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
     * @return [androidx.lifecycle.LiveData] data source as observable having [K] data
     */
    @MainThread
    protected abstract fun loadFromDb(): LiveData<T>


    /**
     * Creates [retrofit2.Call] instance which would be used by [retrofit2.Call] for calling network request.
     *
     * @return [retrofit2.Call]] instance having [V] data source
     */
    @MainThread
    protected abstract fun createCall(): Call<V>?

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
}
