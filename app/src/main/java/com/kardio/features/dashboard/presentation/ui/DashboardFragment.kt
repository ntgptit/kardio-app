package com.kardio.features.dashboard.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentDashboardBinding
import com.kardio.features.dashboard.domain.model.Class
import com.kardio.features.dashboard.domain.model.Folder
import com.kardio.features.dashboard.domain.model.StreakData
import com.kardio.features.dashboard.domain.model.StreakDay
import com.kardio.features.dashboard.domain.model.StudyModule
import com.kardio.features.dashboard.presentation.adapter.StreakDayAdapter
import com.kardio.features.dashboard.presentation.viewmodel.DashboardViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val viewModel: DashboardViewModel by viewModels()
    private val streakDayAdapter = StreakDayAdapter()

    // Views for streak days
    private lateinit var streakDayViews: List<View>

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStreakDayViews()
        setupListeners()
    }

    private fun initStreakDayViews() {
        with(binding) {
            streakDayViews = listOf(
                streakCard.findViewById(R.id.day_9),
                streakCard.findViewById(R.id.day_10),
                streakCard.findViewById(R.id.day_11),
                streakCard.findViewById(R.id.day_12),
                streakCard.findViewById(R.id.day_13),
                streakCard.findViewById(R.id.day_14),
                streakCard.findViewById(R.id.day_15)
            )
        }
    }

    private fun setupListeners() {
        // Notification badge click
        binding.notificationButton.setOnClickListener {
            // Handle notification click
            QlzSnackbar.showInfo(requireContext(), "Notifications clicked")
        }

        // Search click
        binding.searchContainer.setOnClickListener {
            // Navigate to search screen
            QlzSnackbar.showInfo(requireContext(), "Search clicked")
        }

        // Folder clicks
        binding.foldersContainer.findViewById<View>(R.id.grammar_folder).setOnClickListener {
            // Navigate to Grammar folder
            QlzSnackbar.showInfo(requireContext(), "Navigating to Grammar folder")
        }

        binding.foldersContainer.findViewById<View>(R.id.other_folder).setOnClickListener {
            // Navigate to Other folder
            QlzSnackbar.showInfo(requireContext(), "Navigating to Other folder")
        }

        // Study module click
        binding.vitaminStudyCard.setOnClickListener {
            // Navigate to Vitamin study module
            QlzSnackbar.showInfo(requireContext(), "Navigating to Study module")
        }

        // Class click
        binding.koreanClassCard.setOnClickListener {
            // Navigate to Korean class
            QlzSnackbar.showInfo(requireContext(), "Navigating to Class")
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    handleUiState(state)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    handleUiEvent(event)
                }
            }
        }
    }

    private fun handleUiState(state: DashboardViewModel.DashboardUiState) {
        // Show loading if needed
        binding.loadingIndicator.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        // Update folders
        if (state.folders.isNotEmpty()) {
            updateFolders(state.folders)
        }

        // Update study modules
        if (state.studyModules.isNotEmpty()) {
            updateStudyModules(state.studyModules)
        }

        // Update classes
        if (state.classes.isNotEmpty()) {
            updateClasses(state.classes)
        }

        // Update streak data
        state.streakData?.let {
            updateStreakData(it)
        }

        // Show error if any
        state.error?.let {
            QlzSnackbar.showError(requireContext(), it)
        }
    }

    private fun handleUiEvent(event: DashboardViewModel.DashboardUiEvent) {
        when (event) {
            is DashboardViewModel.DashboardUiEvent.NavigateToFolder -> {
                // Navigate to folder
                QlzSnackbar.showInfo(requireContext(), "Navigating to folder: ${event.folderId}")
            }
            is DashboardViewModel.DashboardUiEvent.NavigateToStudyModule -> {
                // Navigate to study module
                QlzSnackbar.showInfo(requireContext(), "Navigating to study module: ${event.moduleId}")
            }
            is DashboardViewModel.DashboardUiEvent.NavigateToClass -> {
                // Navigate to class
                QlzSnackbar.showInfo(requireContext(), "Navigating to class: ${event.classId}")
            }
        }
    }

    private fun updateFolders(folders: List<Folder>) {
        val foldersView = binding.foldersContainer

        // Update Grammar folder
        if (folders.size >= 1) {
            val folder = folders[0]
            with(foldersView) {
                val grammarFolder = findViewById<View>(R.id.grammar_folder)
                grammarFolder.findViewById<TextView>(R.id.grammar_folder_title).text = folder.name
                grammarFolder.findViewById<ImageView>(R.id.grammar_folder_icon)
                    .setColorFilter(ContextCompat.getColor(requireContext(), R.color.accent))
                grammarFolder.findViewById<TextView>(R.id.grammar_folder_username).text = folder.createdByUsername
                grammarFolder.findViewById<View>(R.id.grammar_folder_plus_badge).visibility =
                    if (folder.isCreatedByPlusUser) View.VISIBLE else View.GONE
            }
        }

        // Update Other folder
        if (folders.size >= 2) {
            val folder = folders[1]
            with(foldersView) {
                val otherFolder = findViewById<View>(R.id.other_folder)
                otherFolder.findViewById<TextView>(R.id.other_folder_title).text = folder.name
                otherFolder.findViewById<ImageView>(R.id.other_folder_icon)
                    .setColorFilter(ContextCompat.getColor(requireContext(), R.color.secondary))
                otherFolder.findViewById<TextView>(R.id.other_folder_username).text = folder.createdByUsername
                otherFolder.findViewById<View>(R.id.other_folder_plus_badge).visibility =
                    if (folder.isCreatedByPlusUser) View.VISIBLE else View.GONE
            }
        }
    }

    private fun updateStudyModules(modules: List<StudyModule>) {
        // Update study modules section
        if (modules.isNotEmpty()) {
            val module = modules[0]
            with(binding.vitaminStudyCard) {
                findViewById<TextView>(R.id.study_module_title).text = module.title
                findViewById<TextView>(R.id.study_module_count).text = "${module.termCount} thuật ngữ"
                findViewById<TextView>(R.id.study_module_username).text = module.createdByUsername
                findViewById<View>(R.id.study_module_plus_badge).visibility =
                    if (module.isCreatedByPlusUser) View.VISIBLE else View.GONE
            }
        }
    }

    private fun updateClasses(classes: List<Class>) {
        // Update classes section
        if (classes.isNotEmpty()) {
            val classData = classes[0]
            with(binding.koreanClassCard) {
                findViewById<TextView>(R.id.class_title).text = classData.name
                findViewById<TextView>(R.id.class_modules_count).text = "${classData.studyModuleCount} học phần"
                findViewById<TextView>(R.id.class_members_count).text = "${classData.memberCount} thành viên"
            }
        }
    }

    private fun updateStreakData(streakData: StreakData) {
        // Update streak UI
        with(binding.streakCard) {
            findViewById<TextView>(R.id.streak_count).text = streakData.currentStreak.toString()
            findViewById<TextView>(R.id.streak_title).text = "Chuỗi ${streakData.currentStreak} tuần"

            // Update streak days using the adapter
            streakDayAdapter.bindStreakDays(streakDayViews, streakData.weeklyStreak)
        }
    }
}