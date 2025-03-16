// File: app/src/main/java/com/kardio/features/library/presentation/adapter/StudySetAdapter.kt
package com.kardio.features.library.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kardio.databinding.ItemStudySetLibraryBinding
import com.kardio.features.library.domain.model.StudySet

class StudySetAdapter(
    private val onItemClick: (StudySet) -> Unit
) : ListAdapter<StudySet, StudySetAdapter.StudySetViewHolder>(StudySetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudySetViewHolder {
        val binding = ItemStudySetLibraryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudySetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudySetViewHolder, position: Int) {
        val studySet = getItem(position)
        holder.bind(studySet)
    }

    inner class StudySetViewHolder(
        private val binding: ItemStudySetLibraryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(studySet: StudySet) {
            binding.textTitle.text = studySet.title
            binding.textWordCount.text = "${studySet.wordCount} thuật ngữ"
            binding.textCreator.text = studySet.creatorName
            binding.textPlusBadge.visibility = if (studySet.hasPlusBadge) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }

    }

    class StudySetDiffCallback : DiffUtil.ItemCallback<StudySet>() {
        override fun areItemsTheSame(oldItem: StudySet, newItem: StudySet): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StudySet, newItem: StudySet): Boolean {
            return oldItem == newItem
        }
    }
}