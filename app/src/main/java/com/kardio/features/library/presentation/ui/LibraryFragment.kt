// File: app/src/main/java/com/kardio/features/library/presentation/ui/LibraryFragment.kt
package com.kardio.features.library.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.core.viewmodel.SharedViewModel
import com.kardio.databinding.FragmentLibraryBinding
import com.kardio.features.library.presentation.viewmodel.LibraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LibraryFragment : BaseFragment<FragmentLibraryBinding>() {

    private val viewModel: LibraryViewModel by viewModels()

    // Lấy SharedViewModel từ fragment cha (HomeFragment)
    private val sharedViewModel: SharedViewModel by viewModels({ requireParentFragment() })

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLibraryBinding {
        return FragmentLibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()

        // Thông báo cho SharedViewModel rằng đang ở library
        sharedViewModel.updateSelectedTab(3)
    }

    override fun setupViews() {
        // Kiểm tra adapter của ViewPager để tránh lỗi khi chưa khởi tạo
        binding.btnAdd.setOnClickListener {
            val totalTabs = binding.viewPager.adapter?.itemCount ?: 0
            val currentTab = binding.viewPager.currentItem

            if (totalTabs == 0 || currentTab >= totalTabs) return@setOnClickListener

            when (currentTab) {
//                0 -> navigateToCreateStudyModule() // Xử lý khi tab "Học phần" được chọn
                1 -> navigateToCreateFolder()      // Xử lý khi tab "Thư mục" được chọn
                2 -> navigateToCreateClass()       // Xử lý khi tab "Lớp học" được chọn
            }
        }
    }

    private fun setupViewPager() {
        // Setup ViewPager with TabLayout
        val pagerAdapter = LibraryPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        // Connect TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.library_category_flashcards)
                1 -> getString(R.string.library_category_folders)
                2 -> getString(R.string.library_category_classes)
                else -> ""
            }
        }.attach()

        // Listen for page changes to update FAB
        binding.viewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.updateSelectedTab(position)
            }
        })
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    // Update UI based on state if needed
                }
            }
        }

        // Observe shared data from SharedViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.sharedData.collectLatest { data ->
                    // Xử lý dữ liệu shared nếu cần
                }
            }
        }
    }

    // Navigation methods
//    private fun navigateToCreateStudyModule() {
//        findNavController().navigate(R.id.action_homeFragment_to_createStudyModuleFragment)
//    }

    private fun navigateToCreateFolder() {
        findNavController().navigate(R.id.action_homeFragment_to_createFolderFragment)
    }

    private fun navigateToCreateClass() {
        findNavController().navigate(R.id.action_homeFragment_to_createClassFragment)
    }

    // ViewPager adapter
    private inner class LibraryPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> StudySetsFragment()
                1 -> FoldersFragment()
                2 -> ClassesFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}