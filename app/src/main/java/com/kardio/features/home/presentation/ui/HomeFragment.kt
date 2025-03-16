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
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentHomeBinding
import com.kardio.databinding.LayoutBottomSheetCreateOptionsBinding
import com.kardio.features.dashboard.presentation.ui.DashboardFragment
import com.kardio.features.home.presentation.viewmodel.HomeViewModel
import com.kardio.ui.components.extensions.setIconTint
import com.kardio.ui.components.feedback.QlzSnackbar
import com.kardio.utils.InsetsCompatWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    // Fragment instances
    private var dashboardFragment: Fragment? = null
    private var solutionsFragment: Fragment? = null
    private var libraryFragment: Fragment? = null
    private var profileFragment: Fragment? = null

    // Current displayed fragment
    private var currentFragment: Fragment? = null

    // Keep reference to the current bottom sheet dialog
    private var dialog: BottomSheetDialog? = null

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Thêm callback xử lý quay lại
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (childFragmentManager.backStackEntryCount > 0) {
                    childFragmentManager.popBackStack()
                } else if (viewModel.uiState.value.selectedTabIndex != 0) {
                    // Nếu không ở tab đầu tiên, quay về tab đầu
                    viewModel.changeTab(0)
                    binding.bottomNavBar.selectedItemId = R.id.nav_home
                } else {
                    // Nếu đã ở tab đầu, cho phép activity xử lý back
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup window insets if needed
        setupWindowInsets()

        // Restore state if available, otherwise show default tab
        if (savedInstanceState != null) {
            val selectedTab = savedInstanceState.getInt("selectedTab", 0)
            viewModel.changeTab(selectedTab)
        } else {
            // Show default tab (Dashboard)
            viewModel.changeTab(0)
        }
    }

    override fun setupViews() {
        // Setup Bottom Navigation
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> viewModel.changeTab(0)
                R.id.nav_solutions -> viewModel.changeTab(1)
                R.id.nav_create -> {
                    showCreateModal()
                    return@setOnItemSelectedListener false
                }
                R.id.nav_library -> viewModel.changeTab(3)
                R.id.nav_profile -> viewModel.changeTab(4)
            }

            // Xóa tất cả backstack entries khi chuyển tab để tránh behavior không mong muốn
            childFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)

            true
        }
    }

    private fun showCreateModal() {
        dialog = BottomSheetDialog(requireContext(), R.style.ThemeOverlay_App_BottomSheetDialog)
        val bindingBottomSheet = LayoutBottomSheetCreateOptionsBinding.inflate(layoutInflater)

        // Setup create options
        setupCreateOptions(bindingBottomSheet)

        dialog?.setContentView(bindingBottomSheet.root)
        dialog?.show()
    }

    private fun setupCreateOptions(binding: LayoutBottomSheetCreateOptionsBinding) {
        // Study Module Option
        val studyModuleOption = binding.createStudyModuleOption.root
        val studyModuleIcon = studyModuleOption.findViewById<ImageView>(R.id.icon)
        val studyModuleTitle = studyModuleOption.findViewById<TextView>(R.id.title)
        val studyModuleSubtitle = studyModuleOption.findViewById<TextView>(R.id.subtitle)

        studyModuleIcon.setImageResource(R.drawable.ic_style_outline)
        studyModuleTitle.text = getString(R.string.create_study_module)
        studyModuleSubtitle.text = getString(R.string.create_flashcards_subtitle)
        studyModuleIcon.setColorFilter(requireContext().getColor(R.color.primary))

        studyModuleOption.setOnClickListener {
            dialog?.dismiss()

            // Sử dụng parent NavController để điều hướng sang fragment khác
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_homeFragment_to_createStudyModuleFragment)
        }

        // Folder Option
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

            // Sử dụng parent NavController để điều hướng sang fragment khác
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_homeFragment_to_createFolderFragment)
        }

        // Class Option
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

            // Sử dụng parent NavController để điều hướng sang fragment khác
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_homeFragment_to_createClassFragment)
        }
    }

    private fun setupWindowInsets() {
        InsetsCompatWrapper.setupInsetsForHomeScreen(
            rootView = binding.root,
            bottomNav = binding.bottomNavBar
        )
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    // Update UI based on selected tab
                    updateSelectedTab(state.selectedTabIndex)
                }
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

        // Show the appropriate fragment
        showTabContent(tabIndex)
    }

    private fun showTabContent(tabIndex: Int) {
        // Get or create fragment for the selected tab
        val fragment = getTabFragment(tabIndex)

        // If no change in fragment, do nothing
        if (fragment == currentFragment && currentFragment != null) {
            return
        }

        // Start transaction
        val transaction = childFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)

        // Thay vì hide/show, sử dụng replace để tránh fragment overlap
        transaction.replace(R.id.fragment_container, fragment, "tab_$tabIndex")

        // Commit transaction
        transaction.commitNow() // Sử dụng commitNow để đảm bảo thay đổi được áp dụng ngay lập tức

        // Update current fragment reference
        currentFragment = fragment
    }

    private fun getTabFragment(tabIndex: Int): Fragment {
        return when (tabIndex) {
            0 -> {
                if (dashboardFragment == null) {
                    dashboardFragment = DashboardFragment()
                }
                dashboardFragment!!
            }
            1 -> {
                if (solutionsFragment == null) {
                    solutionsFragment = SolutionsFragment()
                }
                solutionsFragment!!
            }
            3 -> {
                if (libraryFragment == null) {
                    libraryFragment = LibraryFragment()
                }
                libraryFragment!!
            }
            4 -> {
                if (profileFragment == null) {
                    profileFragment = ProfileFragment()
                }
                profileFragment!!
            }
            else -> {
                if (dashboardFragment == null) {
                    dashboardFragment = DashboardFragment()
                }
                dashboardFragment!!
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save selected tab index
        outState.putInt("selectedTab", viewModel.uiState.value.selectedTabIndex)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Dismiss dialog if still showing to prevent window leak
        dialog?.dismiss()
        dialog = null
    }

    // Simple placeholder fragments
    class SolutionsFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.layout_solutions_tab, container, false)
        }
    }

    class LibraryFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.layout_library_tab, container, false)
        }
    }

    class ProfileFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.layout_profile_tab, container, false)
        }
    }
}