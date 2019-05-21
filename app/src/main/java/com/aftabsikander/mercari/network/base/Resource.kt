package com.aftabsikander.mercari.network.base


/**
 * Base Pojo class which will hold all states of the Responses.
 */
class Resource<T> private constructor(
    val status: Status,
    val data: T?,
    val message: String? = "",
    val loading: Boolean = false
) {

    enum class Status { SUCCESS, ERROR, LOADING, OFFLINE, HOST_ERROR }

    companion object {
        fun <T> success(data: T): Resource<T?>? {
            return Resource(Status.SUCCESS, data)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(loadingStatus: Boolean): Resource<T> {
            return Resource(Status.LOADING, null, loading = loadingStatus)
        }

        fun <T> offline(msg: String, data: T?): Resource<T> {
            return Resource(Status.OFFLINE, data, msg)
        }

        fun <T> hostError(msg: String, data: T?): Resource<T> {
            return Resource(Status.HOST_ERROR, data, msg)
        }
    }
}
