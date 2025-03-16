// File: app/src/main/java/com/kardio/features/library/presentation/ui/FoldersFragment.kt
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
import com.kardio.databinding.FragmentFoldersBinding
import com.kardio.features.home.presentation.ui.HomeFragmentDirections
import com.kardio.features.library.domain.model.Folder
import com.kardio.features.library.presentation.adapter.FolderAdapter
import com.kardio.features.library.presentation.viewmodel.FoldersViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoldersFragment : BaseFragment<FragmentFoldersBinding>() {

    private val viewModel: FoldersViewModel by viewModels()
    private lateinit var folderAdapter: FolderAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFoldersBinding {
        return FragmentFoldersBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setupSwipeToRefresh()
    }

    private fun initRecyclerView() {
        folderAdapter = FolderAdapter(
            onItemClick = { folder ->
                navigateToFolderDetail(folder)
            }
        )

        binding.recyclerViewFolders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = folderAdapter
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshFolders()
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

    private fun updateUi(state: FoldersViewModel.FoldersUiState) {
        // Update loading state
        binding.swipeRefreshLayout.isRefreshing = state.isLoading

        // Update views based on content availability
        binding.recyclerViewFolders.isVisible = state.folders.isNotEmpty()
        binding.layoutEmptyState.root.isVisible = state.folders.isEmpty() && !state.isLoading
        binding.layoutErrorState.root.isVisible = state.error != null

        if (state.error != null) {
            binding.layoutErrorState.textViewErrorMessage.text = state.error
            QlzSnackbar.showError(requireContext(), state.error)
            viewModel.errorShown()
        }

        // Update adapter data
        folderAdapter.submitList(state.folders)
    }

    private fun navigateToFolderDetail(folder: Folder) {
        // Example navigation - adjust according to your nav graph
        val action = HomeFragmentDirections.actionHomeFragmentToFolderDetailFragment(
            folder.id,
            folder.name
        )
        findNavController().navigate(action)
    }
}