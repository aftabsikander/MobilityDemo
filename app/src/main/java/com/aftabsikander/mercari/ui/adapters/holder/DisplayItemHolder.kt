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

class DisplayItemHolder(
    private val binding: ItemDisplayRowBinding,
    private val callback: DisplayItemClickListener
) : RecyclerView.ViewHolder(binding.root), ImageRequestListener.Callback {

    companion object {
        fun create(
            inflater: LayoutInflater,
            parent: ViewGroup,
            callback: DisplayItemClickListener
        ): DisplayItemHolder {
            val itemMovieListBinding = ItemDisplayRowBinding.inflate(inflater, parent, false)
            return DisplayItemHolder(itemMovieListBinding, callback)
        }
    }

    fun bindData(displayItem: DisplayItem?) {
        binding.displayItem = displayItem
        binding.executePendingBindings()
        binding.root.setOnClickListener {
            displayItem?.let { item -> callback.commentPressed(item) }
        }
        when (displayItem?.status) {
            DisplayItemStatus.ON_SALE.status -> binding.imgStatus.visibility = View.GONE
            DisplayItemStatus.SOLD_OUT.status -> binding.imgStatus.visibility = View.VISIBLE
            else -> binding.imgStatus.visibility = View.GONE
        }

        displayItem?.imgURL?.let { loadImage(binding.imgDisplay.context, it, it) }
    }


    private fun loadImage(context: Context, thumbLoadUrl: String, fullImageUrl: String) {
        val requestOption = RequestOptions()
            .placeholder(R.drawable.photo_holder)
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


    override fun onFailure(message: String?) {
        Timber.d("Fail to load: $message")
    }

    override fun onSuccess(dataSource: String) {
        Timber.d("Loaded from: $dataSource")
    }

}
