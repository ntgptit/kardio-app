// File: app/src/main/java/com/kardio/core/base/adapter/BaseAdapter.kt
package com.kardio.core.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Base adapter for RecyclerView that handles diffing using ListAdapter.
 * @param T The type of items in the list
 * @param VB The type of ViewBinding for the item views
 */
abstract class BaseAdapter<T, VB : ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseAdapter.BaseViewHolder<VB>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = createBinding(inflater, parent, viewType)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        val item = getItem(position)
        bind(holder.binding, item, position)
    }

    /**
     * Create binding for the item view.
     */
    abstract fun createBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VB

    /**
     * Bind data to the item view.
     */
    abstract fun bind(binding: VB, item: T, position: Int)

    /**
     * ViewHolder for the BaseAdapter.
     */
    class BaseViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}