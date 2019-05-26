package com.aftabsikander.mercari.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.data.DataFetcher
import okhttp3.*
import java.io.IOException
import java.io.InputStream

class ImageStreamFetcher(private val client: Call.Factory, private val url: String) : DataFetcher<InputStream> {
    private var stream: InputStream? = null
    private var responseBody: ResponseBody? = null
    @Volatile
    private var call: Call? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val request = Request.Builder().url(url).build()


        call = client.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onLoadFailed(e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                responseBody = response.body()
                if (response.isSuccessful) {
                    responseBody?.let {
                        callback.onDataReady(it.byteStream())
                    } ?: callback.onLoadFailed(HttpException("respondBody is null", 404))
                } else {
                    callback.onLoadFailed(HttpException(response.message(), response.code()))
                }
            }
        })

    }

    override fun cleanup() {
        stream?.close()
        responseBody?.close()
    }

    override fun cancel() {
        call?.cancel()
    }

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.REMOTE
    }
}