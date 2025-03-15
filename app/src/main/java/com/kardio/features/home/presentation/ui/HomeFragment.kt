// features/home/presentation/ui/HomeFragment.kt
package com.kardio.features.home.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentHomeBinding
import com.kardio.databinding.LayoutBottomSheetCreateOptionsBinding
import com.kardio.features.home.presentation.viewmodel.HomeViewModel
import com.kardio.ui.components.extensions.setIconTint
import com.kardio.utils.InsetsCompatWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup window insets
        setupWindowInsets()
    }

    private fun setupWindowInsets() {
        // Sử dụng helper class để xử lý insets
        InsetsCompatWrapper.setupInsetsForHomeScreen(
            rootView = binding.root,
            appBar = binding.appBar,
            bottomNav = binding.bottomNavBar
        )

        // Thiết lập padding cho các tab containers
        val containers = listOf(
            binding.dashboardContainer,
            binding.solutionsContainer,
            binding.libraryContainer,
            binding.profileContainer
        )

        // Đảm bảo các container không bị che khuất bởi navigation bar
        containers.forEach { container ->
            InsetsCompatWrapper.setupMarginsForView(
                view = container,
                applyBottom = true
            )
        }
    }

    override fun setupViews() {
        // Setup Bottom Navigation
        setupBottomNavigation()

        // Setup AppBar actions
        setupAppBarActions()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> viewModel.changeTab(0)
                R.id.nav_solutions -> viewModel.changeTab(1)
                R.id.nav_create -> showCreateModal()
                R.id.nav_library -> viewModel.changeTab(3)
                R.id.nav_profile -> viewModel.changeTab(4)
            }

            // Return true for all except create button which doesn't change selected tab
            menuItem.itemId != R.id.nav_create
        }
    }

    private fun setupAppBarActions() {
        with(binding.appBar) {
            // Setup menu button
            binding.appBar.findViewById<View>(R.id.app_bar_left_icon)?.setOnClickListener {
                // Open drawer if needed
            }

            // Setup notifications
            binding.notificationButton.setOnClickListener {
                // Handle notification click
            }

            // Setup settings menu
            binding.settingsButton.setOnClickListener {
                showSettingsMenu()
            }
        }
    }

    private fun showSettingsMenu() {
        val popup = android.widget.PopupMenu(requireContext(), binding.settingsButton)
        popup.menuInflater.inflate(R.menu.settings_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_settings -> {
                    // Navigate to settings
                    true
                }
                R.id.menu_logout -> {
                    // Handle logout
                    true
                }
                else -> false
            }
        }

        popup.show()
    }

    private fun showCreateModal() {
        val dialog = BottomSheetDialog(requireContext(), R.style.ThemeOverlay_App_BottomSheetDialog)
        val createOptionsBinding = LayoutBottomSheetCreateOptionsBinding.inflate(layoutInflater)

        // Setup create options
        setupCreateOptions(createOptionsBinding)

        dialog.setContentView(createOptionsBinding.root)
        dialog.show()
    }

    private fun setupCreateOptions(binding: LayoutBottomSheetCreateOptionsBinding) {
        // Study Module Option
        binding.createStudyModuleOption.apply {
            icon.setImageResource(R.drawable.ic_style_outline)
            title.text = getString(R.string.create_study_module)
            subtitle.text = getString(R.string.create_flashcards_subtitle)
            root.setIconTint(requireContext().getColor(R.color.primary))

            root.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_createStudyModuleFragment)
            }
        }

        // Folder Option
        binding.createFolderOption.apply {
            icon.setImageResource(R.drawable.ic_folder_outline)
            title.text = getString(R.string.create_folder)
            subtitle.text = getString(R.string.organize_modules_subtitle)
            root.setIconTint(requireContext().getColor(R.color.warning))

            root.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_createFolderFragment)
            }
        }

        // Class Option
        binding.createClassOption.apply {
            icon.setImageResource(R.drawable.ic_school_outline)
            title.text = getString(R.string.create_class)
            subtitle.text = getString(R.string.create_learning_space_subtitle)
            root.setIconTint(requireContext().getColor(R.color.secondary))

            root.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_createClassFragment)
            }
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                // Update UI based on selected tab
                updateSelectedTab(state.selectedTabIndex)
            }
        }
    }

    private fun updateSelectedTab(tabIndex: Int) {
        // Update selected item in bottom navigation
        val selectedItemId = when (tabIndex) {
            0 -> R.id.nav_home
            1 -> R.id.nav_solutions
            3 -> R.id.nav_library
            4 -> R.id.nav_profile
            else -> R.id.nav_home
        }

        if (binding.bottomNavBar.selectedItemId != selectedItemId) {
            binding.bottomNavBar.selectedItemId = selectedItemId
        }

        // Show the appropriate fragment/content
        showTabContent(tabIndex)
    }

    private fun showTabContent(tabIndex: Int) {
        // Hide all content containers first
        binding.dashboardContainer.visibility = View.GONE
        binding.solutionsContainer.visibility = View.GONE
        binding.libraryContainer.visibility = View.GONE
        binding.profileContainer.visibility = View.GONE

        // Show the selected container
        when (tabIndex) {
            0 -> binding.dashboardContainer.visibility = View.VISIBLE
            1 -> binding.solutionsContainer.visibility = View.VISIBLE
            3 -> binding.libraryContainer.visibility = View.VISIBLE
            4 -> binding.profileContainer.visibility = View.VISIBLE
        }
    }
}