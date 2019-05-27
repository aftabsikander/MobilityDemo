package com.aftabsikander.mercari.callbacks

import com.aftabsikander.mercari.network.models.DisplayItem

/**
 * Display Item Callback Interface which will be used when [com.aftabsikander.mercari.network.models.DisplayItem] is clicked
 */
interface DisplayItemClickListener {

    /**
     * Handle display item click event
     * @param item [DisplayItem] instance
     */
    fun displayItemPressed(item: DisplayItem)

    /**
     * Handle photo like click event
     * @param item [DisplayItem] instance
     */
    fun photoLiked(item: DisplayItem)

    /**
     * Handle Comment click event
     * @param item [DisplayItem] instance
     */
    fun commentPressed(item: DisplayItem)
}