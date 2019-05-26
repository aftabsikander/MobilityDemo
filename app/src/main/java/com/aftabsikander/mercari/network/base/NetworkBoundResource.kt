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
 * This class act as the decider to cache the response/ fetch from the service always
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

    @MainThread
    protected abstract fun loadFromDb(): LiveData<T>

    @MainThread
    private fun shouldShowOfflineData(): Boolean {
        return true
    }

    @MainThread
    protected abstract fun createCall(): Call<V>?

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
