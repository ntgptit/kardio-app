// File: app/src/main/java/com/kardio/core/base/adapter/BaseDiffCallback.kt
package com.kardio.core.base.adapter

import androidx.recyclerview.widget.DiffUtil

/**
 * Base DiffUtil.ItemCallback for RecyclerView adapters.
 * @param T The type of items in the list
 * @param itemSame Function to check if two items represent the same object
 * @param contentSame Function to check if two items have the same content
 */
class BaseDiffCallback<T : Any>(
    private val itemSame: (T, T) -> Boolean,
    private val contentSame: (T, T) -> Boolean
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return itemSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return contentSame(oldItem, newItem)
    }
}