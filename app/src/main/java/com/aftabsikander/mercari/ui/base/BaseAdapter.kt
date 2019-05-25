package com.aftabsikander.mercari.ui.base

import androidx.recyclerview.widget.RecyclerView

/**
 * Generic Base adapter for recycler views
 */
abstract class BaseAdapter<T : RecyclerView.ViewHolder, D> : RecyclerView.Adapter<T>() {

    abstract fun setData(data: List<D>)
}