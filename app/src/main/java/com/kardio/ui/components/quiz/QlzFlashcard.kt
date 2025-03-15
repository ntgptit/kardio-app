// File: app/src/main/java/com/kardio/ui/components/quiz/QlzFlashcard.kt
package com.kardio.ui.components.quiz

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kardio.R

/**
 * QlzFlashcard - Component hiển thị flashcard 2 mặt
 * Hỗ trợ hiệu ứng lật thẻ với animation
 */
class QlzFlashcard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var frontView: View
    private lateinit var backView: View
    private lateinit var frontTextView: TextView
    private lateinit var backTextView: TextView

    private var frontText: String? = null
    private var backText: String? = null
    private var isShowingFront = true
    private var isFlipEnabled = true

    private var onFlipListener: ((Boolean) -> Unit)? = null

    init {
        inflateLayout()
        obtainAttributes(attrs)
        setupViews()
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_flashcard, this, true)

        frontView = view.findViewById(R.id.flashcard_front)
        backView = view.findViewById(R.id.flashcard_back)
        frontTextView = view.findViewById(R.id.flashcard_front_text)
        backTextView = view.findViewById(R.id.flashcard_back_text)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzFlashcard)
        try {
            frontText = typedArray.getString(R.styleable.QlzFlashcard_qlzFrontText)
            backText = typedArray.getString(R.styleable.QlzFlashcard_qlzBackText)
            isFlipEnabled = typedArray.getBoolean(R.styleable.QlzFlashcard_qlzFlipEnabled, true)
            isShowingFront = typedArray.getBoolean(R.styleable.QlzFlashcard_qlzShowFront, true)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupViews() {
        // Set text
        frontTextView.text = frontText
        backTextView.text = backText

        // Set initial visibility
        if (isShowingFront) {
            frontView.visibility = View.VISIBLE
            frontView.alpha = 1f
            backView.visibility = View.GONE
            backView.alpha = 0f
        } else {
            frontView.visibility = View.GONE
            frontView.alpha = 0f
            backView.visibility = View.VISIBLE
            backView.alpha = 1f
        }

        // Set click listener
        setOnClickListener {
            if (isFlipEnabled) {
                flipCard()
            }
        }
    }

    /**
     * Flip the card
     */
    fun flipCard() {
        isShowingFront = !isShowingFront

        val showView = if (isShowingFront) frontView else backView
        val hideView = if (isShowingFront) backView else frontView

        // Create animation
        val duration = resources.getInteger(R.integer.anim_duration_medium).toLong()

        // First half of animation - hide current side
        val hideAnimator = ObjectAnimator.ofFloat(hideView, "rotationY", 0f, 90f)
        hideAnimator.duration = duration / 2
        hideAnimator.interpolator = AccelerateDecelerateInterpolator()

        // Second half of animation - show other side
        val showAnimator = ObjectAnimator.ofFloat(showView, "rotationY", -90f, 0f)
        showAnimator.duration = duration / 2
        showAnimator.interpolator = AccelerateDecelerateInterpolator()

        // Combine animations
        val animSet = AnimatorSet()
        animSet.play(showAnimator).after(hideAnimator)

        // Handle visibility changes
        hideAnimator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                hideView.visibility = View.GONE
                showView.visibility = View.VISIBLE
            }
        })

        // Start animation
        animSet.start()

        // Notify listener
        onFlipListener?.invoke(isShowingFront)
    }

    /**
     * Set the front text
     */
    fun setFrontText(text: String) {
        frontText = text
        frontTextView.text = text
    }

    /**
     * Get the front text
     */
    fun getFrontText(): String {
        return frontTextView.text.toString()
    }

    /**
     * Set the back text
     */
    fun setBackText(text: String) {
        backText = text
        backTextView.text = text
    }

    /**
     * Get the back text
     */
    fun getBackText(): String {
        return backTextView.text.toString()
    }

    /**
     * Show the front side without animation
     */
    fun showFront() {
        if (!isShowingFront) {
            isShowingFront = true
            frontView.visibility = View.VISIBLE
            frontView.alpha = 1f
            backView.visibility = View.GONE
            backView.alpha = 0f
        }
    }

    /**
     * Show the back side without animation
     */
    fun showBack() {
        if (isShowingFront) {
            isShowingFront = false
            frontView.visibility = View.GONE
            frontView.alpha = 0f
            backView.visibility = View.VISIBLE
            backView.alpha = 1f
        }
    }

    /**
     * Check if the card is showing the front side
     */
    fun isShowingFront(): Boolean {
        return isShowingFront
    }

    /**
     * Enable or disable flipping
     */
    fun setFlipEnabled(enabled: Boolean) {
        isFlipEnabled = enabled
    }

    /**
     * Set a listener for flip events
     */
    fun setOnFlipListener(listener: (Boolean) -> Unit) {
        onFlipListener = listener
    }
}