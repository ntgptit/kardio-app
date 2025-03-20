// app/src/main/java/com/kardio/features/home/presentation/ui/HomeFragment.kt

package com.kardio.features.home.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.core.viewmodel.SharedViewModel
import com.kardio.databinding.FragmentHomeBinding
import com.kardio.databinding.LayoutBottomSheetCreateOptionsBinding
import com.kardio.utils.InsetsCompatWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    // ViewModel được chia sẻ với các Fragment con
    private val sharedViewModel: SharedViewModel by viewModels()
    private var dialog: BottomSheetDialog? = null

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment_container) as? NavHostFragment
                val navController = navHostFragment?.navController

                // Nếu không ở dashboard, quay về dashboard
                if (navController?.currentDestination?.id != R.id.dashboardFragment) {
                    navController?.navigate(R.id.dashboardFragment)
                    binding.bottomNavBar.selectedItemId = R.id.dashboardFragment
                    sharedViewModel.updateSelectedTab(0)
                } else {
                    // Nếu đã ở dashboard, cho phép back bình thường
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindowInsets()

        // Thiết lập NavHost và Bottom Navigation
        setupNavigation()

        // Khôi phục tab đã chọn nếu có
        if (savedInstanceState != null) {
            val selectedTab = savedInstanceState.getInt("selectedTab", 0)
            sharedViewModel.updateSelectedTab(selectedTab)
        } else {
            sharedViewModel.updateSelectedTab(0)
        }
    }

    override fun setupViews() {
        setupBottomNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // Observer destination changes để cập nhật tab
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.dashboardFragment -> sharedViewModel.updateSelectedTab(0)
                R.id.solutionsFragment -> sharedViewModel.updateSelectedTab(1)
                R.id.libraryFragment -> sharedViewModel.updateSelectedTab(3)
                R.id.profileFragment -> sharedViewModel.updateSelectedTab(4)
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.dashboardFragment -> {
                    navigateToTab(R.id.dashboardFragment)
                    sharedViewModel.updateSelectedTab(0)
                    return@setOnItemSelectedListener true
                }
                R.id.solutionsFragment -> {
                    navigateToTab(R.id.solutionsFragment)
                    sharedViewModel.updateSelectedTab(1)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_create -> {
                    showCreateModal()
                    return@setOnItemSelectedListener false
                }
                R.id.libraryFragment -> {
                    navigateToTab(R.id.libraryFragment)
                    sharedViewModel.updateSelectedTab(3)
                    return@setOnItemSelectedListener true
                }
                R.id.profileFragment -> {
                    navigateToTab(R.id.profileFragment)
                    sharedViewModel.updateSelectedTab(4)
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun navigateToTab(destinationId: Int) {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // Nếu đang không ở destination đích, điều hướng đến đó
        if (navController.currentDestination?.id != destinationId) {
            navController.navigate(destinationId)
        }
    }

    private fun showCreateModal() {
        dialog = BottomSheetDialog(requireContext(), R.style.ThemeOverlay_App_BottomSheetDialog)
        val bindingBottomSheet = LayoutBottomSheetCreateOptionsBinding.inflate(layoutInflater)
        setupCreateOptions(bindingBottomSheet)
        dialog?.setContentView(bindingBottomSheet.root)
        dialog?.show()
    }

    private fun setupCreateOptions(binding: LayoutBottomSheetCreateOptionsBinding) {
        // Cài đặt các tùy chọn tạo (giữ nguyên code cũ)
        val studyModuleOption = binding.createStudyModuleOption.root
        val studyModuleIcon = studyModuleOption.findViewById<ImageView>(R.id.icon)
        val studyModuleTitle = studyModuleOption.findViewById<TextView>(R.id.title)
        val studyModuleSubtitle = studyModuleOption.findViewById<TextView>(R.id.subtitle)
        studyModuleIcon.setImageResource(R.drawable.ic_style_outline)
        studyModuleTitle.text = getString(R.string.create_study_module)
        studyModuleSubtitle.text = getString(R.string.create_flashcards_subtitle)
        studyModuleIcon.setColorFilter(requireContext().getColor(R.color.primary))
        studyModuleOption.setOnClickListener { dialog?.dismiss() }

        val folderOption = binding.createFolderOption.root
        val folderIcon = folderOption.findViewById<ImageView>(R.id.icon)
        val folderTitle = folderOption.findViewById<TextView>(R.id.title)
        val folderSubtitle = folderOption.findViewById<TextView>(R.id.subtitle)
        folderIcon.setImageResource(R.drawable.ic_folder_outline)
        folderTitle.text = getString(R.string.create_folder)
        folderSubtitle.text = getString(R.string.organize_modules_subtitle)
        folderIcon.setColorFilter(requireContext().getColor(R.color.warning))
        folderOption.setOnClickListener {
            dialog?.dismiss()
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_homeFragment_to_createFolderFragment)
        }

        val classOption = binding.createClassOption.root
        val classIcon = classOption.findViewById<ImageView>(R.id.icon)
        val classTitle = classOption.findViewById<TextView>(R.id.title)
        val classSubtitle = classOption.findViewById<TextView>(R.id.subtitle)
        classIcon.setImageResource(R.drawable.ic_school_outline)
        classTitle.text = getString(R.string.create_class)
        classSubtitle.text = getString(R.string.create_learning_space_subtitle)
        classIcon.setColorFilter(requireContext().getColor(R.color.secondary))
        classOption.setOnClickListener {
            dialog?.dismiss()
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_homeFragment_to_createClassFragment)
        }
    }

    private fun setupWindowInsets() {
        InsetsCompatWrapper.setupInsetsForHomeScreen(binding.root, binding.bottomNavBar)
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.selectedTabIndex.collectLatest { tabIndex ->
                    updateSelectedTab(tabIndex)
                }
            }
        }
    }

    private fun updateSelectedTab(tabIndex: Int) {
        val selectedItemId = when (tabIndex) {
            0 -> R.id.dashboardFragment
            1 -> R.id.solutionsFragment
            3 -> R.id.libraryFragment
            4 -> R.id.profileFragment
            else -> R.id.dashboardFragment
        }

        if (binding.bottomNavBar.selectedItemId != selectedItemId) {
            binding.bottomNavBar.selectedItemId = selectedItemId
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedTab", sharedViewModel.selectedTabIndex.value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.dismiss()
        dialog = null
    }
}