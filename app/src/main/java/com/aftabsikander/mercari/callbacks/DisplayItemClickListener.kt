package com.aftabsikander.mercari.callbacks

import com.aftabsikander.mercari.network.models.DisplayItem

interface DisplayItemClickListener {

    fun displayItemPressed(item: DisplayItem)

    fun photoLiked(item: DisplayItem)

    fun commentPressed(item: DisplayItem)
}