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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.core.viewmodel.SharedViewModel
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
import com.kardio.features.home.presentation.ui.HomeFragmentDirections
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    // Lấy ViewModel từ fragment cha (HomeFragment) và DashboardViewModel
    private val sharedViewModel: SharedViewModel by viewModels({ requireParentFragment() })
    private val viewModel: DashboardViewModel by viewModels()

    // Adapters
    private val streakDayAdapter = StreakDayAdapter()
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

        // Thông báo cho SharedViewModel rằng đang ở dashboard
        sharedViewModel.updateSelectedTab(0)

        setupViews()
        observeData()
    }

    override fun setupViews() {
        initStreakDayViews()
        setupAdapters()
        setupListeners()
    }

    private fun initStreakDayViews() {
        streakDayViews = listOf(
            binding.streakCard.day16.root,
            binding.streakCard.day17.root,
            binding.streakCard.day18.root,
            binding.streakCard.day19.root,
            binding.streakCard.day20.root,
            binding.streakCard.day21.root,
            binding.streakCard.day22.root
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

        // Setup RecyclerViews using ViewBinding
        binding.foldersContainer.foldersRecycler.apply {
            adapter = foldersAdapter
            clipToPadding = false
        }

        binding.studyModulesContainer.studyModulesRecycler.apply {
            adapter = studyModulesAdapter
            clipToPadding = false
        }

        binding.classesContainer.classesRecycler.apply {
            adapter = classesAdapter
            clipToPadding = false
        }
    }

    private fun setupListeners() {
        // Notification badge click
        binding.notificationButton.setOnClickListener {
            QlzSnackbar.showInfo(requireContext(), "Notifications clicked")
        }

        // Search click
        binding.searchContainer.searchPlaceholder.setOnClickListener {
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

        // Observe shared data from parent ViewModel if needed
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.sharedData.collectLatest { data ->
                    // Handle shared data if needed
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
                navigateToFolder(event.folderId)
            }
            is DashboardViewModel.DashboardUiEvent.NavigateToStudyModule -> {
                navigateToStudyModule(event.moduleId)
            }
            is DashboardViewModel.DashboardUiEvent.NavigateToClass -> {
                navigateToClass(event.classId)
            }
        }
    }

    private fun navigateToFolder(folderId: String) {
        // Use the parent fragment's NavController to navigate
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        val action = HomeFragmentDirections.actionHomeFragmentToFolderDetailFragment(
            folderId = folderId,
            folderName = "" // You may need to get the folder name from your state
        )
        navController.navigate(action)
    }

    private fun navigateToStudyModule(moduleId: String) {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        val action = HomeFragmentDirections.actionHomeFragmentToModuleDetailFragment(moduleId)
        navController.navigate(action)
    }

    private fun navigateToClass(classId: String) {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        val action = HomeFragmentDirections.actionHomeFragmentToClassDetailFragment(
            classId = classId,
            className = "" // You may need to get the class name from your state
        )
        navController.navigate(action)
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
        // Set streak count and title using ViewBinding
        binding.streakCard.streakCount.text = streakData.currentStreak.toString()
        binding.streakCard.streakTitle.text = "Chuỗi ${streakData.currentStreak} tuần"

        // Update streak days using the adapter
        streakDayAdapter.bindStreakDays(streakDayViews, streakData.weeklyStreak)
    }
}