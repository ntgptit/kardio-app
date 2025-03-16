package com.kardio.features.dashboard.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.kardio.R
import com.kardio.features.dashboard.domain.model.Folder

/**
 * Adapter to display folders in a horizontal scrolling list.
 * Supports dynamic loading from API
 */
class FolderAdapter(
    private val onFolderClicked: (Folder) -> Unit
) : ListAdapter<Folder, FolderAdapter.FolderViewHolder>(FolderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = getItem(position)
        holder.bind(folder)
    }

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView as MaterialCardView
        private val folderIcon: ImageView = itemView.findViewById(R.id.folder_icon)
        private val folderTitle: TextView = itemView.findViewById(R.id.folder_title)
        private val folderAvatar: ShapeableImageView = itemView.findViewById(R.id.folder_avatar)
        private val folderUsername: TextView = itemView.findViewById(R.id.folder_username)
        private val folderPlusBadge: TextView = itemView.findViewById(R.id.folder_plus_badge)

        init {
            cardView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFolderClicked(getItem(position))
                }
            }
        }

        fun bind(folder: Folder) {
            folderTitle.text = folder.name
            folderUsername.text = folder.createdByUsername
            folderPlusBadge.visibility = if (folder.isCreatedByPlusUser) View.VISIBLE else View.GONE

            // Set folder icon color
            folderIcon.setColorFilter(folder.iconColor)
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