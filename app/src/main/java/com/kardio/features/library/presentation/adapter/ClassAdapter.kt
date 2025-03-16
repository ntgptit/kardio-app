// File: app/src/main/java/com/kardio/features/library/presentation/adapter/ClassAdapter.kt
package com.kardio.features.library.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kardio.databinding.ItemClassLibraryBinding
import com.kardio.features.library.domain.model.ClassModel
import com.kardio.R

class ClassAdapter(
    private val onItemClick: (ClassModel) -> Unit
) : ListAdapter<ClassModel, ClassAdapter.ClassViewHolder>(ClassDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemClassLibraryBinding.inflate(
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
        private val binding: ItemClassLibraryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(classModel: ClassModel) {
            binding.textGroupName.text = classModel.name

            // Hiển thị số học phần
            binding.textModuleCount.text = "${classModel.studyModulesCount} học phần"

            // Không có `textViewClassDescription` trong XML nên loại bỏ
            // Kiểm tra nếu có creatorName thì hiển thị, ngược lại ẩn đi
//            if (!classModel.creatorName.isNullOrEmpty()) {
//                binding.text.text = "Tạo bởi: ${classModel.creatorName}"
//                binding.textCreator.visibility = android.view.View.VISIBLE
//            } else {
//                binding.textCreator.visibility = android.view.View.GONE
//            }

            // Hiển thị hoặc ẩn icon quyền (imageViewPermission) nếu allowMembersToAdd = true
            binding.imageGroupIcon.setImageResource(R.drawable.ic_group)  // Đảm bảo icon hiển thị đúng
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