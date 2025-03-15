// File: app/src/main/java/com/kardio/ui/components/cards/QlzQuizCard.kt
package com.kardio.ui.components.cards

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.kardio.R

/**
 * QlzQuizCard - Card hiển thị câu hỏi quiz hoặc flashcard
 * Được tối ưu cho Dark Mode với màu từ colors.xml
 */
class QlzQuizCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : QlzBaseCard(context, attrs, defStyleAttr) {

    private lateinit var questionTextView: TextView
    private lateinit var indexTextView: TextView

    private var questionText: String? = null
    private var indexText: String? = null
    private var questionTextSize: Float = 0f

    init {
        inflateLayout()
        obtainAttributes(attrs)
        setupViews()
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_quiz_card, this, true)

        questionTextView = view.findViewById(R.id.quiz_card_question)
        indexTextView = view.findViewById(R.id.quiz_card_index)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzQuizCard)
        try {
            questionText = typedArray.getString(R.styleable.QlzQuizCard_qlzQuestionText)
            indexText = typedArray.getString(R.styleable.QlzQuizCard_qlzIndexText)
            questionTextSize = typedArray.getDimension(
                R.styleable.QlzQuizCard_qlzQuestionTextSize,
                resources.getDimension(R.dimen.text_size_xl)
            )
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupViews() {
        // Apply data to views
        questionTextView.text = questionText
        indexTextView.text = indexText

        // Apply text size
        questionTextView.textSize = questionTextSize / resources.displayMetrics.density

        // Set text colors from theme
        questionTextView.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
        indexTextView.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
    }

    // Getter/Setter methods
    fun setQuestionText(text: String) {
        questionText = text
        questionTextView.text = text
    }

    fun setIndexText(text: String) {
        indexText = text
        indexTextView.text = text
    }

    fun setQuestionTextSize(size: Float) {
        questionTextSize = size
        questionTextView.textSize = size / resources.displayMetrics.density
    }
}