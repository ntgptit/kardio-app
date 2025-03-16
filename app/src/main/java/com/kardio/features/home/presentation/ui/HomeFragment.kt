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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentHomeBinding
import com.kardio.databinding.LayoutBottomSheetCreateOptionsBinding
import com.kardio.features.dashboard.presentation.ui.DashboardFragment
import com.kardio.features.home.presentation.viewmodel.HomeViewModel
import com.kardio.features.library.presentation.ui.LibraryFragment
import com.kardio.ui.components.extensions.setIconTint
import com.kardio.utils.InsetsCompatWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private var currentFragment: Fragment? = null
    private var dialog: BottomSheetDialog? = null

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (childFragmentManager.backStackEntryCount > 0) {
                    childFragmentManager.popBackStack()
                } else if (viewModel.uiState.value.selectedTabIndex != 0) {
                    viewModel.changeTab(0)
                    binding.bottomNavBar.selectedItemId = R.id.nav_home
                } else {
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
        if (savedInstanceState != null) {
            val selectedTab = savedInstanceState.getInt("selectedTab", 0)
            viewModel.changeTab(selectedTab)
        } else {
            viewModel.changeTab(0)
        }
    }

    override fun setupViews() {
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
            childFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
            true
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
                viewModel.uiState.collectLatest { state ->
                    updateSelectedTab(state.selectedTabIndex)
                }
            }
        }
    }

    private fun updateSelectedTab(tabIndex: Int) {
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
        showTabContent(tabIndex)
    }

    private fun showTabContent(tabIndex: Int) {
        val fragmentTag = "tab_$tabIndex"
        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)

        // Ẩn Fragment hiện tại nếu có
        currentFragment?.let { transaction.hide(it) }

        // Tìm hoặc tạo Fragment mới
        var fragment = fragmentManager.findFragmentByTag(fragmentTag)
        if (fragment == null) {
            fragment = when (tabIndex) {
                0 -> DashboardFragment()
                1 -> SolutionsFragment()
                3 -> LibraryFragment()
                4 -> ProfileFragment()
                else -> DashboardFragment()
            }
            transaction.add(R.id.fragment_container, fragment, fragmentTag)
        } else {
            transaction.show(fragment)
        }

        transaction.commitNow()
        currentFragment = fragment
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedTab", viewModel.uiState.value.selectedTabIndex)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.dismiss()
        dialog = null
    }

    class SolutionsFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.layout_solutions_tab, container, false)
        }
    }

    class ProfileFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.layout_profile_tab, container, false)
        }
    }
}