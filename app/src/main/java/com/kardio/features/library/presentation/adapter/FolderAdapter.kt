// File: app/src/main/java/com/kardio/features/library/presentation/adapter/FolderAdapter.kt
package com.kardio.features.library.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kardio.databinding.ItemFolderLibraryBinding
import com.kardio.features.library.domain.model.Folder

class FolderAdapter(
    private val onItemClick: (Folder) -> Unit
) : ListAdapter<Folder, FolderAdapter.FolderViewHolder>(FolderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderLibraryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = getItem(position)
        holder.bind(folder)
    }

    inner class FolderViewHolder(
        private val binding: ItemFolderLibraryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(folder: Folder) {
            binding.textFolderName.text = folder.name

            // Nếu folder.description không rỗng, hiển thị nó, ngược lại hiển thị số học phần
            binding.textFolderName.text = folder.description.takeIf { it.isNotEmpty() }
                ?: "Thư mục chứa ${folder.moduleCount} học phần"

            binding.textCreator.text = folder.creatorName

            // Hiển thị hoặc ẩn badge "Plus"
            binding.textPlusBadge.visibility = if (folder.hasPlusBadge) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }

    }

    class FolderDiffCallback : DiffUtil.ItemCallback<Folder>() {
        override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
            return oldItem == newItem
        }
    }
}