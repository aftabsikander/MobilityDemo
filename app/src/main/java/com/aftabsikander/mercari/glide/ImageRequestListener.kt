package com.aftabsikander.mercari.glide

import android.graphics.drawable.Drawable
import com.aftabsikander.mercari.glide.ImageRequestListener.Callback
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition


/**
 * Image Request listener class which handles the load failure and Resource Ready callbacks provided by [RequestListener]
 * @param callback [Callback] instance for image loading events
 *
 * @see [RequestListener]
 */
class ImageRequestListener(private val callback: Callback? = null) : RequestListener<Drawable> {

    /**
     * Network Image loading callbacks
     */
    interface Callback {

        /**
         *  Failure event callback when fail to load image from network or cache.
         *  @param message failure message when unable to load requested image.
         */
        fun onFailure(message: String?)

        /**
         * Image load callback for successful event.
         * @param dataSource Source from where image was loaded.
         */
        fun onSuccess(dataSource: String)
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any,
        target: Target<Drawable>,
        isFirstResource: Boolean
    ): Boolean {

        callback?.onFailure(e?.message)
        return false
    }

    override fun onResourceReady(
        resource: Drawable,
        model: Any,
        target: Target<Drawable>,
        dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {

        callback?.onSuccess(dataSource.toString())
        target.onResourceReady(resource, DrawableCrossFadeTransition(1000, isFirstResource))
        return true

    }
}