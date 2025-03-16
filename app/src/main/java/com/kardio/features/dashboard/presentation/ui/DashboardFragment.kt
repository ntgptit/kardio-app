package com.kardio.features.dashboard.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import com.kardio.features.dashboard.presentation.adapter.FolderAdapter
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
    private lateinit var folderAdapter: FolderAdapter

    // Views for streak days
    private lateinit var streakDayViews: List<View>

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStreakDayViews()
        setupFolderRecyclerView()
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

    private fun setupFolderRecyclerView() {
        folderAdapter = FolderAdapter { folder ->
            viewModel.navigateToFolder(folder.id)
        }

        binding.root.findViewById<RecyclerView>(R.id.folders_recycler_view).apply {
            adapter = folderAdapter
            // Sử dụng ItemDecoration nếu cần thêm spacing
        }

        // Hiển thị RecyclerView và ẩn layout tĩnh
        binding.root.findViewById<RecyclerView>(R.id.folders_recycler_view).visibility = View.VISIBLE
        binding.root.findViewById<View>(R.id.folders_container).visibility = View.GONE
    }

    private fun setupListeners() {
        val rootView = binding.root

        // Notification badge click
        rootView.findViewById<View>(R.id.notification_button).setOnClickListener {
            QlzSnackbar.showInfo(requireContext(), "Notifications clicked")
        }

        // Search click
        rootView.findViewById<View>(R.id.search_placeholder).setOnClickListener {
            QlzSnackbar.showInfo(requireContext(), "Search clicked")
        }

        // View all folders
        rootView.findViewById<View>(R.id.view_all_folders).setOnClickListener {
            QlzSnackbar.showInfo(requireContext(), "View all folders clicked")
        }

        // View all study modules
        rootView.findViewById<View>(R.id.view_all_study_modules).setOnClickListener {
            QlzSnackbar.showInfo(requireContext(), "View all study modules clicked")
        }

        // View all classes
        rootView.findViewById<View>(R.id.view_all_classes).setOnClickListener {
            QlzSnackbar.showInfo(requireContext(), "View all classes clicked")
        }

        // Study module click
        rootView.findViewById<View>(R.id.vitamin_study_card).setOnClickListener {
            if (viewModel.uiState.value.studyModules.isNotEmpty()) {
                viewModel.navigateToStudyModule(viewModel.uiState.value.studyModules[0].id)
            }
        }

        // Class click
        rootView.findViewById<View>(R.id.korean_class_card).setOnClickListener {
            if (viewModel.uiState.value.classes.isNotEmpty()) {
                viewModel.navigateToClass(viewModel.uiState.value.classes[0].id)
            }
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
        binding.root.findViewById<View>(R.id.loading_indicator).visibility = if (state.isLoading) View.VISIBLE else View.GONE

        // Update folders with RecyclerView
        if (state.folders.isNotEmpty()) {
            folderAdapter.submitList(state.folders)
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

    private fun updateStudyModules(modules: List<StudyModule>) {
        if (modules.isNotEmpty()) {
            val module = modules[0]
            val rootView = binding.root
            rootView.findViewById<TextView>(R.id.study_module_title).text = module.title
            rootView.findViewById<TextView>(R.id.study_module_count).text = "${module.termCount} thuật ngữ"
            rootView.findViewById<TextView>(R.id.study_module_username).text = module.createdByUsername
            rootView.findViewById<View>(R.id.study_module_plus_badge).visibility =
                if (module.isCreatedByPlusUser) View.VISIBLE else View.GONE
        }
    }

    private fun updateClasses(classes: List<Class>) {
        if (classes.isNotEmpty()) {
            val classData = classes[0]
            val rootView = binding.root
            rootView.findViewById<TextView>(R.id.class_title).text = classData.name
            rootView.findViewById<TextView>(R.id.class_modules_count).text = "${classData.studyModuleCount} học phần"
            rootView.findViewById<TextView>(R.id.class_members_count).text = "${classData.memberCount} thành viên"
        }
    }

    private fun updateStreakData(streakData: StreakData) {
        val rootView = binding.root
        // Sử dụng findViewById thông qua binding.streakCard
        rootView.findViewById<TextView>(R.id.streak_count).text = streakData.currentStreak.toString()
        rootView.findViewById<TextView>(R.id.streak_title).text = "Chuỗi ${streakData.currentStreak} tuần"

        // Update streak days using the adapter
        streakDayAdapter.bindStreakDays(streakDayViews, streakData.weeklyStreak)
    }
}