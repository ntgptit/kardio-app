package com.kardio.features.dashboard.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentDashboardBinding
import com.kardio.features.dashboard.domain.model.Class
import com.kardio.features.dashboard.domain.model.Folder
import com.kardio.features.dashboard.domain.model.StreakData
import com.kardio.features.dashboard.domain.model.StudyModule
import com.kardio.features.dashboard.presentation.adapter.ClassAdapter
import com.kardio.features.dashboard.presentation.adapter.FolderAdapter
import com.kardio.features.dashboard.presentation.adapter.StreakDayAdapter
import com.kardio.features.dashboard.presentation.adapter.StudyModuleAdapter
import com.kardio.features.dashboard.presentation.viewmodel.DashboardViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val viewModel: DashboardViewModel by viewModels()
    private val streakDayAdapter = StreakDayAdapter()

    // Adapters for horizontal scrolling lists
    private lateinit var foldersAdapter: FolderAdapter
    private lateinit var studyModulesAdapter: StudyModuleAdapter
    private lateinit var classesAdapter: ClassAdapter

    // Views for streak days
    private lateinit var streakDayViews: List<View>

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStreakDayViews()
        setupAdapters()
        setupListeners()
    }

    private fun initStreakDayViews() {
        val rootView = binding.root
        streakDayViews = listOf(
            rootView.findViewById(R.id.day_9),
            rootView.findViewById(R.id.day_10),
            rootView.findViewById(R.id.day_11),
            rootView.findViewById(R.id.day_12),
            rootView.findViewById(R.id.day_13),
            rootView.findViewById(R.id.day_14),
            rootView.findViewById(R.id.day_15)
        )
    }

    private fun setupAdapters() {
        // Initialize folders adapter
        foldersAdapter = FolderAdapter().apply {
            setOnItemClickListener { folder ->
                viewModel.navigateToFolder(folder.id)
            }
        }

        // Initialize study modules adapter
        studyModulesAdapter = StudyModuleAdapter().apply {
            setOnItemClickListener { module ->
                viewModel.navigateToStudyModule(module.id)
            }
        }

        // Initialize classes adapter
        classesAdapter = ClassAdapter().apply {
            setOnItemClickListener { classItem ->
                viewModel.navigateToClass(classItem.id)
            }
        }

        // Setup RecyclerViews
        val rootView = binding.root

        rootView.findViewById<RecyclerView>(R.id.folders_recycler).apply {
            adapter = foldersAdapter
            clipToPadding = false
        }

        rootView.findViewById<RecyclerView>(R.id.study_modules_recycler).apply {
            adapter = studyModulesAdapter
            clipToPadding = false
        }

        rootView.findViewById<RecyclerView>(R.id.classes_recycler).apply {
            adapter = classesAdapter
            clipToPadding = false
        }
    }

    private fun setupListeners() {
        val rootView = binding.root

        // Notification badge click
        binding.notificationButton.setOnClickListener {
            QlzSnackbar.showInfo(requireContext(), "Notifications clicked")
        }

        // Search click
        rootView.findViewById<View>(R.id.search_placeholder).setOnClickListener {
            QlzSnackbar.showInfo(requireContext(), "Search clicked")
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
                QlzSnackbar.showInfo(requireContext(), "Navigating to folder: ${event.folderId}")
            }
            is DashboardViewModel.DashboardUiEvent.NavigateToStudyModule -> {
                QlzSnackbar.showInfo(requireContext(), "Navigating to study module: ${event.moduleId}")
            }
            is DashboardViewModel.DashboardUiEvent.NavigateToClass -> {
                QlzSnackbar.showInfo(requireContext(), "Navigating to class: ${event.classId}")
            }
        }
    }

    private fun updateFolders(folders: List<Folder>) {
        // Update folders RecyclerView using adapter
        foldersAdapter.setItems(folders)
    }

    private fun updateStudyModules(modules: List<StudyModule>) {
        // Update study modules RecyclerView using adapter
        studyModulesAdapter.setItems(modules)
    }

    private fun updateClasses(classes: List<Class>) {
        // Update classes RecyclerView using adapter
        classesAdapter.setItems(classes)
    }

    private fun updateStreakData(streakData: StreakData) {
        val rootView = binding.root
        // Set streak count and title
        rootView.findViewById<TextView>(R.id.streak_count).text = streakData.currentStreak.toString()
        rootView.findViewById<TextView>(R.id.streak_title).text = "Chuỗi ${streakData.currentStreak} tuần"

        // Update streak days using the adapter
        streakDayAdapter.bindStreakDays(streakDayViews, streakData.weeklyStreak)
    }
}