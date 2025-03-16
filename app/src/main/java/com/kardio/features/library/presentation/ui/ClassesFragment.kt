// File: app/src/main/java/com/kardio/features/library/presentation/ui/ClassesFragment.kt
package com.kardio.features.library.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentClassesBinding
import com.kardio.features.home.presentation.ui.HomeFragmentDirections
import com.kardio.features.library.domain.model.ClassModel
import com.kardio.features.library.presentation.adapter.ClassAdapter
import com.kardio.features.library.presentation.viewmodel.ClassesViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClassesFragment : BaseFragment<FragmentClassesBinding>() {

    private val viewModel: ClassesViewModel by viewModels()
    private lateinit var classAdapter: ClassAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentClassesBinding {
        return FragmentClassesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setupSwipeToRefresh()
    }

    private fun initRecyclerView() {
        classAdapter = ClassAdapter(
            onItemClick = { classModel ->
                navigateToClassDetail(classModel)
            }
        )

        binding.recyclerViewClasses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = classAdapter
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshClasses()
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    updateUi(state)
                }
            }
        }
    }

    private fun updateUi(state: ClassesViewModel.ClassesUiState) {
        // Update loading state
        binding.swipeRefreshLayout.isRefreshing = state.isLoading

        // Update views based on content availability
        binding.recyclerViewClasses.isVisible = state.classes.isNotEmpty()
        binding.layoutEmptyState.root.isVisible = state.classes.isEmpty() && !state.isLoading
        binding.layoutErrorState.root.isVisible = state.error != null

        if (state.error != null) {
            binding.layoutErrorState.textViewErrorMessage.text = state.error
            QlzSnackbar.showError(requireContext(), state.error)
            viewModel.errorShown()
        }

        // Update adapter data
        classAdapter.submitList(state.classes)
    }

    private fun navigateToClassDetail(classModel: ClassModel) {
        // Example navigation - adjust according to your nav graph
        val action = HomeFragmentDirections.actionHomeFragmentToClassDetailFragment(
            classModel.id,
            classModel.name
        )
        findNavController().navigate(action)
    }
}