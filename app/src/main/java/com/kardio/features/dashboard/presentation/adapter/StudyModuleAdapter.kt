package com.kardio.features.dashboard.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.kardio.R
import com.kardio.features.dashboard.domain.model.StudyModule

class StudyModuleAdapter : RecyclerView.Adapter<StudyModuleAdapter.StudyModuleViewHolder>() {

    private val items = mutableListOf<StudyModule>()
    private var onItemClickListener: ((StudyModule) -> Unit)? = null

    fun setItems(newItems: List<StudyModule>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (StudyModule) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyModuleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_study_module, parent, false)
        return StudyModuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudyModuleViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class StudyModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.study_module_title)
        private val countTextView: TextView = itemView.findViewById(R.id.study_module_count)
        private val usernameTextView: TextView = itemView.findViewById(R.id.study_module_username)
        private val plusBadge: View = itemView.findViewById(R.id.study_module_plus_badge)
        private val avatarView: ShapeableImageView = itemView.findViewById(R.id.study_module_avatar)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(items[position])
                }
            }
        }

        fun bind(module: StudyModule) {
            titleTextView.text = module.title
            countTextView.text = "${module.termCount} thuật ngữ"
            usernameTextView.text = module.createdByUsername
            plusBadge.visibility =
                if (module.isCreatedByPlusUser) View.VISIBLE else View.GONE
        }
    }
}