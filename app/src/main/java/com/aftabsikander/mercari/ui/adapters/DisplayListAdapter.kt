package com.aftabsikander.mercari.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.aftabsikander.mercari.callbacks.DisplayItemClickListener
import com.aftabsikander.mercari.network.models.DisplayItem
import com.aftabsikander.mercari.ui.adapters.holder.DisplayItemHolder

/**
 * Display item Adapter which holds all the setup and configure values for it's population
 *
 * @param itemClick [DisplayItemClickListener] display item click listener callback
 */
class DisplayListAdapter(
    private val itemClick: DisplayItemClickListener
) : PagedListAdapter<DisplayItem, DisplayItemHolder>(displayItemDiffCallback) {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, i: Int): DisplayItemHolder {
        return DisplayItemHolder.create(LayoutInflater.from(parent.context), parent, itemClick)
    }

    override fun onBindViewHolder(@NonNull holder: DisplayItemHolder, position: Int) {
        holder.bindData(getItem(position))
    }


    companion object {
        val displayItemDiffCallback = object : DiffUtil.ItemCallback<DisplayItem>() {
            override fun areItemsTheSame(oldItem: DisplayItem, newItem: DisplayItem): Boolean {
                return oldItem.uniqueID == newItem.uniqueID
            }

            override fun areContentsTheSame(oldItem: DisplayItem, newItem: DisplayItem): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }


}



