package com.kardio.ui.components.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.kardio.R

/**
 * QlzProfileHeader - Header hiển thị thông tin profile người dùng
 * Bao gồm avatar, username, streak count
 */
class QlzProfileHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var avatarView: ShapeableImageView  // Thay đổi từ ImageView sang ShapeableImageView
    private lateinit var usernameTextView: TextView
    private lateinit var streakCountTextView: TextView
    private lateinit var streakIconView: ImageView

    private var username: String? = null
    private var streakCount: Int = 0
    private var avatarResId: Int = R.drawable.ic_profile_placeholder

    private var onAvatarClickListener: (() -> Unit)? = null

    init {
        inflateLayout()
        setupFromAttributes(attrs)
        setupViews()
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_profile_header, this, true)

        avatarView = view.findViewById(R.id.profile_avatar)
        usernameTextView = view.findViewById(R.id.profile_username)
        streakCountTextView = view.findViewById(R.id.profile_streak_count)
        streakIconView = view.findViewById(R.id.profile_streak_icon)
    }

    private fun setupFromAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.QlzProfileHeader
        )

        try {
            username = typedArray.getString(R.styleable.QlzProfileHeader_qlzUsername)
            streakCount = typedArray.getInteger(
                R.styleable.QlzProfileHeader_qlzStreakCount, 0
            )
            avatarResId = typedArray.getResourceId(
                R.styleable.QlzProfileHeader_qlzAvatarIcon,
                R.drawable.ic_profile_placeholder
            )
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupViews() {
        // Set username
        usernameTextView.text = username

        // Set streak count
        updateStreakCount(streakCount)

        // Set avatar
        avatarView.setImageResource(avatarResId)

        // Set click listener
        avatarView.setOnClickListener {
            onAvatarClickListener?.invoke()
        }

        // Set streak icon tint
        streakIconView.setColorFilter(ContextCompat.getColor(context, R.color.streak_flame_orange))
    }

    /**
     * Update the streak count
     */
    private fun updateStreakCount(count: Int) {
        streakCount = count

        if (count > 0) {
            streakCountTextView.text = count.toString()
            streakCountTextView.visibility = View.VISIBLE
            streakIconView.visibility = View.VISIBLE
        } else {
            streakCountTextView.visibility = View.GONE
            streakIconView.visibility = View.GONE
        }
    }

    /**
     * Set the username
     */
    fun setUsername(username: String) {
        this.username = username
        usernameTextView.text = username
    }

    /**
     * Set the streak count
     */
    fun setStreakCount(count: Int) {
        updateStreakCount(count)
    }

    /**
     * Set the avatar icon
     */
    fun setAvatarIcon(resId: Int) {
        avatarResId = resId
        avatarView.setImageResource(resId)
    }

    /**
     * Set listener for avatar click
     */
    fun setOnAvatarClickListener(listener: () -> Unit) {
        onAvatarClickListener = listener
    }
}