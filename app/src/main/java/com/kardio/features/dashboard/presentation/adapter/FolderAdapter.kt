package com.kardio.features.dashboard.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.kardio.R
import com.kardio.features.dashboard.domain.model.Folder

class FolderAdapter : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    private val items = mutableListOf<Folder>()
    private var onItemClickListener: ((Folder) -> Unit)? = null

    fun setItems(newItems: List<Folder>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Folder) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconView: ImageView = itemView.findViewById(R.id.folder_icon)
        private val titleTextView: TextView = itemView.findViewById(R.id.folder_title)
        private val usernameTextView: TextView = itemView.findViewById(R.id.folder_username)
        private val plusBadge: View = itemView.findViewById(R.id.folder_plus_badge)
        private val avatarView: ShapeableImageView = itemView.findViewById(R.id.folder_avatar)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(items[position])
                }
            }
        }

        fun bind(folder: Folder) {
            titleTextView.text = folder.name
            usernameTextView.text = folder.createdByUsername
            plusBadge.visibility =
                if (folder.isCreatedByPlusUser) View.VISIBLE else View.GONE

            // Set folder icon color
            iconView.setColorFilter(folder.iconColor)
        }
    }
}