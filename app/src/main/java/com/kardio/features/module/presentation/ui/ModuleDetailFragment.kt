// features/module/presentation/ui/ModuleDetailFragment.kt
package com.kardio.features.module.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.snackbar.Snackbar
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentModuleDetailBinding
import com.kardio.features.module.domain.model.Flashcard
import com.kardio.features.module.presentation.adapter.FlashcardCarouselAdapter
import com.kardio.features.module.presentation.viewmodel.ModuleDetailViewModel
import com.kardio.utils.extensions.setupWithViewPager2
import com.kardio.utils.helpers.AnimationUtils
import com.kardio.utils.helpers.DateTimeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

@AndroidEntryPoint
class ModuleDetailFragment : BaseFragment<FragmentModuleDetailBinding>() {

    private val viewModel: ModuleDetailViewModel by viewModels()
    private lateinit var flashcardAdapter: FlashcardCarouselAdapter

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentModuleDetailBinding {
        return FragmentModuleDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFlashcardCarousel()
        setupListeners()
        observeViewModel()
    }

    private fun setupFlashcardCarousel() {
        flashcardAdapter = FlashcardCarouselAdapter(
            onStarToggled = { flashcardId, isStarred ->
                viewModel.onFlashcardStarToggled(flashcardId, isStarred)
            })

        binding.flashcardsCarousel.viewPagerFlashcards.apply {
            adapter = flashcardAdapter

            // Set up page transformer for card effect
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(40))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }

            setPageTransformer(compositePageTransformer)

            // Connect to tab indicator
            binding.flashcardsCarousel.tabLayoutIndicator.setupWithViewPager2(this)
        }
    }

    private fun setupListeners() {
        with(binding) {
            // App bar navigation
            appBar.setNavigationOnClickListener {
                viewModel.onNavigateBack()
            }

            // Study option buttons
            studyModeOptionsContainer.optionFlashcards.setOnClickListener {
                AnimationUtils.applyButtonClickAnimation(it, requireContext())
                viewModel.onStartLearningClick()
            }

            studyModeOptionsContainer.optionQuiz.setOnClickListener {
                AnimationUtils.applyButtonClickAnimation(it, requireContext())
                viewModel.onStartQuizClick()
            }

            studyModeOptionsContainer.optionMatch.setOnClickListener {
                AnimationUtils.applyButtonClickAnimation(it, requireContext())
                viewModel.onStartMatchClick()
            }

            // Fullscreen button in carousel
            flashcardsCarousel.fabFullscreen.setOnClickListener {
                AnimationUtils.applyButtonClickAnimation(it, requireContext())
                viewModel.onStartLearningClick() // Fullscreen is same as starting flashcard study
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                updateUI(state)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collectLatest { event ->
                handleEvent(event)
            }
        }
    }

    private fun updateUI(state: ModuleDetailViewModel.ModuleDetailUiState) {
        with(binding) {
            // Update loading state
            val hasFlashcards = state.flashcards.isNotEmpty()

            // Show empty state or flashcards
            textEmptyFlashcards.isVisible = !hasFlashcards && !state.isLoading
            flashcardsCarousel.root.isVisible = hasFlashcards && !state.isLoading

            // Update flashcards adapter if we have data
            if (hasFlashcards) {
                flashcardAdapter.submitList(state.flashcards)

                // Update tab indicator count
                flashcardsCarousel.tabLayoutIndicator.removeAllTabs()
                repeat(state.flashcards.size) {
                    flashcardsCarousel.tabLayoutIndicator.addTab(
                        flashcardsCarousel.tabLayoutIndicator.newTab()
                    )
                }
            }

            // Update module details
            state.module?.let { module ->
                appBar.title = module.title
                textModuleTitle.text = module.title

                // Update creator info
                val creatorName = state.creator?.name ?: module.creatorName
                creatorInfoContainer.textCreatorName.text = creatorName

                // Show last updated time
                val formattedDate = DateTimeUtils.getTimeAgo(module.lastUpdated)
                creatorInfoContainer.textCreatedDate.text =
                    getString(R.string.last_updated, formattedDate)

                // If we have a creator avatar URL
                state.creator?.avatar?.let { avatarUrl ->
                    // Here we would load the avatar image using Coil or Glide
                    // For example: creatorInfoContainer.imageCreatorAvatar.load(avatarUrl)
                }
            }
        }
    }

    private fun handleEvent(event: ModuleDetailViewModel.ModuleDetailEvent) {
        when (event) {
            is ModuleDetailViewModel.ModuleDetailEvent.ShowErrorMessage -> {
                Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
            }
            is ModuleDetailViewModel.ModuleDetailEvent.NavigateBack -> {
                findNavController().navigateUp()
            }
            is ModuleDetailViewModel.ModuleDetailEvent.NavigateToFlashcardStudy -> {
                Timber.d("Navigating to Flashcard Study for module: ${event.moduleId}")
                // Ví dụ: findNavController().navigate(R.id.action_to_flashcard_study)
            }
            is ModuleDetailViewModel.ModuleDetailEvent.NavigateToQuiz -> {
                Timber.d("Navigating to Quiz for module: ${event.moduleId}")
                // Ví dụ: findNavController().navigate(R.id.action_to_quiz)
            }
            is ModuleDetailViewModel.ModuleDetailEvent.NavigateToMatch -> {
                Timber.d("Navigating to Match for module: ${event.moduleId}")
                // Ví dụ: findNavController().navigate(R.id.action_to_match)
            }
        }
    }
}

