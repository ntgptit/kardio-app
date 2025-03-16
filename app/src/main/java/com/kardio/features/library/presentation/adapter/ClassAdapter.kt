// File: app/src/main/java/com/kardio/features/library/presentation/adapter/ClassAdapter.kt
package com.kardio.features.library.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kardio.databinding.ItemClassBinding
import com.kardio.features.library.domain.model.ClassModel

class ClassAdapter(
    private val onItemClick: (ClassModel) -> Unit
) : ListAdapter<ClassModel, ClassAdapter.ClassViewHolder>(ClassDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemClassBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classModel = getItem(position)
        holder.bind(classModel)
    }

    inner class ClassViewHolder(
        private val binding: ItemClassBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(classModel: ClassModel) {
            binding.textViewClassName.text = classModel.name
            binding.textViewClassDescription.text = classModel.description.takeIf { it.isNotEmpty() }
                ?: "Lớp học chứa ${classModel.studyModulesCount} học phần"
            binding.textViewModuleCount.text = "${classModel.studyModulesCount} học phần"

            // Check if creator name exists
            if (classModel.creatorName != null && classModel.creatorName.isNotEmpty()) {
                binding.textViewCreator.text = "Tạo bởi: ${classModel.creatorName}"
                binding.textViewCreator.visibility = android.view.View.VISIBLE
            } else {
                binding.textViewCreator.visibility = android.view.View.GONE
            }

            // Show permission indicator
            binding.imageViewPermission.visibility = if (classModel.allowMembersToAdd) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }
    }

    class ClassDiffCallback : DiffUtil.ItemCallback<ClassModel>() {
        override fun areItemsTheSame(oldItem: ClassModel, newItem: ClassModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ClassModel, newItem: ClassModel): Boolean {
            return oldItem == newItem
        }
    }
}