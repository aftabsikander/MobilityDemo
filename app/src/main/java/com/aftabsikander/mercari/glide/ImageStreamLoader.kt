package com.aftabsikander.mercari.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import okhttp3.OkHttpClient
import java.io.InputStream

class ImageStreamLoader(private val client: OkHttpClient) : ModelLoader<ImageModel, InputStream> {

    class Factory(private val client: OkHttpClient) : ModelLoaderFactory<ImageModel, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ImageModel, InputStream> {
            return ImageStreamLoader(client)
        }

        override fun teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }

    override fun buildLoadData(displayImage: ImageModel, width: Int, height: Int, options: Options)
            : ModelLoader.LoadData<InputStream>? {
        val url = displayImage.imageUrl
        return ModelLoader.LoadData(ObjectKey(url), ImageStreamFetcher(client, url))
    }

    override fun handles(displayImage: ImageModel): Boolean {
        return true
    }
}