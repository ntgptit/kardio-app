package com.kardio.features.dashboard.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kardio.R
import com.kardio.features.dashboard.domain.model.Class

class ClassAdapter : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    private val items = mutableListOf<Class>()
    private var onItemClickListener: ((Class) -> Unit)? = null

    fun setItems(newItems: List<Class>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Class) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_class, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.class_title)
        private val modulesCountTextView: TextView = itemView.findViewById(R.id.class_modules_count)
        private val membersCountTextView: TextView = itemView.findViewById(R.id.class_members_count)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(items[position])
                }
            }
        }

        fun bind(classItem: Class) {
            titleTextView.text = classItem.name
            modulesCountTextView.text = "${classItem.studyModuleCount} học phần"
            membersCountTextView.text = "${classItem.memberCount} thành viên"
        }
    }
}