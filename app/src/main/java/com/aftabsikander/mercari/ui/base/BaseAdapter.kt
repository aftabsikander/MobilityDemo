package com.aftabsikander.mercari.ui.base

import androidx.recyclerview.widget.RecyclerView

/**
 * Generic Base adapter for recycler views
 * @param T instance of [RecyclerView.ViewHolder] view holder instance.
 * @param D data source type which will be used for the [RecyclerView.Adapter]
 */
abstract class BaseAdapter<T : RecyclerView.ViewHolder, D> : RecyclerView.Adapter<T>() {

    /**
     * Add/Update existing data source for [RecyclerView.Adapter]
     */
    abstract fun setData(data: List<D>)
}