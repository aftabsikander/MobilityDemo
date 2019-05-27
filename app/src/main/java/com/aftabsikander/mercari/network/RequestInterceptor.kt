package com.aftabsikander.mercari.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection


/**
 * Request Interceptor for Retrofit which automatically send Refresh token call if we receive
 * [java.net.HttpURLConnection.HTTP_UNAUTHORIZED] status code.
 * If we fail to acquire new token we force logout user.
 *
 * @see [okhttp3.Interceptor]
 */
class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()
        setHeaders(builder)
        request = builder.build()
        val response = chain.proceed(request)
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            //TODO refresh token
        }

        return response
    }

    /**
     * Set Authorization Header for Retrofit client
     * @param builder [Request.Builder] object
     */
    private fun setHeaders(builder: Request.Builder) {
        builder.addHeader("Content-Type", "application/json")
    }
}