// features/module/presentation/adapter/FlashcardCarouselAdapter.kt
package com.kardio.features.module.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kardio.R
import com.kardio.databinding.ItemFlashcardBinding
import com.kardio.features.module.domain.model.Flashcard
import com.kardio.utils.helpers.AnimationUtils

class FlashcardCarouselAdapter(
    private val onStarToggled: (String, Boolean) -> Unit,
    private val onAudioPlay: ((String) -> Unit)? = null
) : ListAdapter<Flashcard, FlashcardCarouselAdapter.FlashcardViewHolder>(FLASHCARD_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val binding = ItemFlashcardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FlashcardViewHolder(binding, onStarToggled, onAudioPlay)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FlashcardViewHolder(
        private val binding: ItemFlashcardBinding,
        private val onStarToggled: (String, Boolean) -> Unit,
        private val onAudioPlay: ((String) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentFlashcard: Flashcard? = null
        private var isShowingFront = true

        init {
            // Set up click listener for the entire card to flip
            binding.flashcardContainer.setOnClickListener {
                flipCard()
            }

            // Star/difficult button (front side)
            binding.btnDifficult.setOnClickListener {
                currentFlashcard?.let { card ->
                    onStarToggled(card.id, !card.isStarred)
                }
            }

            // Star/difficult button (back side)
            binding.btnDifficultBack.setOnClickListener {
                currentFlashcard?.let { card ->
                    onStarToggled(card.id, !card.isStarred)
                }
            }

            // Audio button
            binding.btnAudio.setOnClickListener {
                currentFlashcard?.let { card ->
                    if (card.audioUrl != null) {
                        onAudioPlay?.invoke(card.audioUrl)
                    }
                }
            }
        }

        fun bind(flashcard: Flashcard) {
            currentFlashcard = flashcard

            // Reset to front side when binding a new card
            resetToFrontSide()

            // Populate front side content
            binding.textTerm.text = flashcard.term

            // Handle pronunciation - show only if available
            if (flashcard.pronunciation.isNullOrBlank()) {
                binding.textPronunciation.visibility = View.GONE
            } else {
                binding.textPronunciation.text = flashcard.pronunciation
                binding.textPronunciation.visibility = View.VISIBLE
            }

            // Set audio button visibility based on availability
            binding.btnAudio.visibility = if (flashcard.audioUrl != null) View.VISIBLE else View.GONE

            // Populate back side content
            binding.textDefinition.text = flashcard.definition

            // Handle example - show only if available
            if (flashcard.example.isNullOrBlank()) {
                binding.textExampleLabel.visibility = View.GONE
                binding.textExample.visibility = View.GONE
            } else {
                binding.textExampleLabel.visibility = View.VISIBLE
                binding.textExample.visibility = View.VISIBLE
                binding.textExample.text = flashcard.example
            }

            // Update star icon on both sides based on star status
            updateStarIcons(flashcard.isStarred)
        }

        private fun resetToFrontSide() {
            isShowingFront = true
            binding.flashcardFront.visibility = View.VISIBLE
            binding.flashcardFront.alpha = 1f
            binding.flashcardBack.visibility = View.GONE
        }

        private fun flipCard() {
            if (isShowingFront) {
                // Flip to back
                binding.flashcardFront.animate()
                    .alpha(0f)
                    .setDuration(150)
                    .withEndAction {
                        binding.flashcardFront.visibility = View.GONE
                        binding.flashcardBack.alpha = 0f
                        binding.flashcardBack.visibility = View.VISIBLE
                        binding.flashcardBack.animate()
                            .alpha(1f)
                            .setDuration(150)
                            .start()
                    }
                    .start()
            } else {
                // Flip to front
                binding.flashcardBack.animate()
                    .alpha(0f)
                    .setDuration(150)
                    .withEndAction {
                        binding.flashcardBack.visibility = View.GONE
                        binding.flashcardFront.alpha = 0f
                        binding.flashcardFront.visibility = View.VISIBLE
                        binding.flashcardFront.animate()
                            .alpha(1f)
                            .setDuration(150)
                            .start()
                    }
                    .start()
            }

            isShowingFront = !isShowingFront
        }

        private fun updateStarIcons(isStarred: Boolean) {
            val iconRes = if (isStarred) R.drawable.ic_star else R.drawable.ic_star_outline

            // Update both front and back difficult buttons
            binding.btnDifficult.setIconResource(iconRes)
            binding.btnDifficultBack.setIconResource(iconRes)
        }
    }

    companion object {
        private val FLASHCARD_COMPARATOR = object : DiffUtil.ItemCallback<Flashcard>() {
            override fun areItemsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
                return oldItem == newItem
            }
        }
    }
}