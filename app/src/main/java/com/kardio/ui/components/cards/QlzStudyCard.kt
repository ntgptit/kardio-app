// File: app/src/main/java/com/kardio/ui/components/cards/QlzStudyCard.kt
package com.kardio.ui.components.cards

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kardio.R

/**
 * QlzStudyCard - Component cho study deck/card hiển thị trên home screen.
 * Sử dụng màu flashcard_icon (#2EACDC) cho icon
 */
class QlzStudyCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : QlzBaseCard(context, attrs, defStyleAttr) {

    private lateinit var iconView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var subtitleTextView: TextView
    private lateinit var countTextView: TextView
    private lateinit var container: ConstraintLayout

    private var title: String? = null
    private var subtitle: String? = null
    private var count: String? = null
    private var iconResId: Int = R.drawable.ic_flashcard_icon

    init {
        inflateLayout()
        obtainAttributes(attrs)
        setupViews()
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_study_card, this, true)

        iconView = view.findViewById(R.id.study_card_icon)
        titleTextView = view.findViewById(R.id.study_card_title)
        subtitleTextView = view.findViewById(R.id.study_card_subtitle)
        countTextView = view.findViewById(R.id.study_card_count)
        container = view.findViewById(R.id.study_card_container)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzStudyCard)
        try {
            title = typedArray.getString(R.styleable.QlzStudyCard_qlzTitle)
            subtitle = typedArray.getString(R.styleable.QlzStudyCard_qlzSubtitle)
            count = typedArray.getString(R.styleable.QlzStudyCard_qlzCount)
            iconResId = typedArray.getResourceId(
                R.styleable.QlzStudyCard_qlzIcon,
                R.drawable.ic_flashcard_icon
            )
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupViews() {
        // Apply data to views
        titleTextView.text = title
        subtitleTextView.text = subtitle
        countTextView.text = count
        iconView.setImageResource(iconResId)

        // Set icon tint to flashcard color
        iconView.setColorFilter(
            ContextCompat.getColor(context, R.color.flashcard_icon)
        )

        // Thiết lập màu cho text
        titleTextView.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
        subtitleTextView.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
        countTextView.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
    }

    // Getter/Setter methods
    fun setTitle(text: String) {
        title = text
        titleTextView.text = text
    }

    fun setSubtitle(text: String) {
        subtitle = text
        subtitleTextView.text = text
    }

    fun setCount(text: String) {
        count = text
        countTextView.text = text
    }

    fun setIcon(resId: Int) {
        iconResId = resId
        iconView.setImageResource(resId)
    }
}
