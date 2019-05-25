package com.aftabsikander.mercari.ui.adapters.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aftabsikander.mercari.callbacks.DisplayItemClickListener
import com.aftabsikander.mercari.databinding.ItemDisplayRowBinding
import com.aftabsikander.mercari.network.models.DisplayItem
import com.aftabsikander.mercari.utilities.constants.DisplayItemStatus

class DisplayItemHolder(
    private val binding: ItemDisplayRowBinding,
    private val callback: DisplayItemClickListener
) : RecyclerView.ViewHolder(binding.root) {


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

    }

}
