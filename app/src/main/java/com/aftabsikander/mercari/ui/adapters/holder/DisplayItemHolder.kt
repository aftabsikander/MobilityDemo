package com.aftabsikander.mercari.ui.adapters.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aftabsikander.mercari.R
import com.aftabsikander.mercari.callbacks.DisplayItemClickListener
import com.aftabsikander.mercari.databinding.ItemDisplayRowBinding
import com.aftabsikander.mercari.glide.ImageModel
import com.aftabsikander.mercari.glide.ImageRequestListener
import com.aftabsikander.mercari.network.models.DisplayItem
import com.aftabsikander.mercari.utilities.constants.DisplayItemStatus
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import timber.log.Timber

/**
 * Display Item View holder for [RecyclerView.ViewHolder] which holds all its setup and configurations.
 *
 * @param binding View binder instance for the layout [ItemDisplayRowBinding]
 * @param callback [DisplayItemClickListener] instance for item click events
 */
class DisplayItemHolder(
    private val binding: ItemDisplayRowBinding,
    private val callback: DisplayItemClickListener
) : RecyclerView.ViewHolder(binding.root), ImageRequestListener.Callback {

    companion object {

        /**
         * Create Instance of [DisplayItemHolder] which will be used by adapter
         * @param inflater [LayoutInflater] instance for populating layout
         * @param parent [ViewGroup] instance from parent view.
         * @param callback [DisplayItemClickListener] instance for item click events
         *
         * @return [DisplayItemHolder] instance
         */
        fun create(
            inflater: LayoutInflater, parent: ViewGroup, callback: DisplayItemClickListener
        ): DisplayItemHolder {
            val itemMovieListBinding = ItemDisplayRowBinding.inflate(inflater, parent, false)
            return DisplayItemHolder(itemMovieListBinding, callback)
        }
    }

    /**
     * Binds data to layout view against [DisplayItem] item.
     * @param displayItem [DisplayItem] instance for reading values
     */
    fun bindData(displayItem: DisplayItem?) {
        binding.displayItem = displayItem
        binding.executePendingBindings()
        when (displayItem?.status) {
            DisplayItemStatus.ON_SALE.status -> binding.imgStatus.visibility = View.GONE
            DisplayItemStatus.SOLD_OUT.status -> binding.imgStatus.visibility = View.VISIBLE
            else -> binding.imgStatus.visibility = View.GONE
        }
        setupClickListener(displayItem)
        displayItem?.imgURL?.let { loadImage(binding.imgDisplay.context, it, it) }
    }


    /**
     * Setup click listener events on views.
     * @param displayItem [DisplayItem] instance
     */
    private fun setupClickListener(displayItem: DisplayItem?) {
        binding.root.setOnClickListener {
            displayItem?.let { item -> callback.displayItemPressed(item) }
        }

        binding.imgComment.setOnClickListener {
            displayItem?.let { item -> callback.commentPressed(item) }
        }

        binding.imgFav.setOnClickListener {
            displayItem?.let { item -> callback.photoLiked(item) }
        }
    }

    /**
     * Load image from network using [Glide].
     * @param context Calling context.
     * @param thumbLoadUrl Thumbnail image URL for faster load.
     * @param fullImageUrl Full image URL
     */
    private fun loadImage(context: Context, thumbLoadUrl: String, fullImageUrl: String) {
        val requestOption = RequestOptions()
            .placeholder(R.drawable.photo_holder)
            .error(R.drawable.photo_holder)
        Glide.with(context).load(ImageModel(fullImageUrl))
            .transition(DrawableTransitionOptions.withCrossFade())
            .thumbnail(
                Glide.with(context)
                    .load(ImageModel(thumbLoadUrl))
                    .apply(requestOption)
            )
            .apply(requestOption)
            .listener(ImageRequestListener(this))
            .into(binding.imgDisplay)
    }


    /**
     *  Failure event callback when fail to load image from network or cache.
     *  @param message failure message when unable to load requested image.
     */
    override fun onFailure(message: String?) {
        Timber.d("Fail to load: $message")
    }

    /**
     * Image load callback for successful event.
     * @param dataSource Source from where image was loaded.
     */
    override fun onSuccess(dataSource: String) {
        Timber.d("Loaded from: $dataSource")
    }


}
