package com.aftabsikander.mercari.glide

import android.content.Context
import com.aftabsikander.mercari.MercariApp
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.MemoryCategory
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import java.io.InputStream

@GlideModule
class ImageGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val client = MercariApp.getInstance().appComponent.getOkHttpClient()
        val devicePerformance = MercariApp.getInstance().appComponent.getPerformanceChecker()

        if (devicePerformance.getDevicePerformanceResult()) {
            glide.setMemoryCategory(MemoryCategory.NORMAL)
        } else {
            glide.setMemoryCategory(MemoryCategory.LOW)
        }

        registry.append(ImageModel::class.java, InputStream::class.java, ImageStreamLoader.Factory(client))
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val performanceDetect = MercariApp.getInstance().appComponent.getPerformanceChecker()
        if (performanceDetect.getDevicePerformanceResult()) {
            builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
        } else {
            builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
        }
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}