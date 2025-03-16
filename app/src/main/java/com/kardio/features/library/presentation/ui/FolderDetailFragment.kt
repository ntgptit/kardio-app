// File: app/src/main/java/com/kardio/features/library/presentation/ui/FolderDetailFragment.kt
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentFolderDetailBinding
import com.kardio.features.library.domain.model.StudySet
import com.kardio.features.library.presentation.adapter.StudySetAdapter
import com.kardio.features.library.presentation.viewmodel.FolderDetailViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FolderDetailFragment : BaseFragment<FragmentFolderDetailBinding>() {

    private val viewModel: FolderDetailViewModel by viewModels()
    private val args: FolderDetailFragmentArgs by navArgs()
    private lateinit var studySetAdapter: StudySetAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFolderDetailBinding {
        return FragmentFolderDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setupSwipeToRefresh()
        loadFolderData()
    }

    private fun loadFolderData() {
        viewModel.loadStudySetsByFolder(args.folderId)
    }

    private fun initRecyclerView() {
        studySetAdapter = StudySetAdapter(
            onItemClick = { studySet ->
//                navigateToStudySetDetail(studySet)
            }
        )

        binding.recyclerViewStudySets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = studySetAdapter
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshStudySets()
        }
    }

    override fun setupViews() {
        // Set folder name in toolbar
        binding.toolbar.title = args.folderName

        // Setup back button
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Setup add study module FAB
//        binding.fabAddStudySet.setOnClickListener {
////            navigateToCreateStudyModule()
//        }
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

    private fun updateUi(state: FolderDetailViewModel.FolderDetailUiState) {
        // Update loading state
        binding.swipeRefreshLayout.isRefreshing = state.isLoading

        // Update views based on content availability
        binding.recyclerViewStudySets.isVisible = state.studySets.isNotEmpty()
        binding.layoutEmptyState.root.isVisible = state.studySets.isEmpty() && !state.isLoading
        binding.layoutErrorState.root.isVisible = state.error != null

        if (state.error != null && !state.error.isNullOrEmpty()) {
            binding.layoutErrorState.textViewErrorMessage.text = state.error
            QlzSnackbar.showError(requireContext(), state.error)
            viewModel.errorShown()
        }

        // Update adapter data
        studySetAdapter.submitList(state.studySets)
    }

//    private fun navigateToStudySetDetail(studySet: StudySet) {
//        // Example navigation - adjust according to your nav graph
//        val action = FolderDetailFragmentDirections.actionFolderDetailFragmentToStudySetDetailFragment(
//            studySet.id,
//            studySet.title
//        )
//        findNavController().navigate(action)
//    }

//    private fun navigateToCreateStudyModule() {
//        // Example navigation - adjust according to your nav graph
//        val action = FolderDetailFragmentDirections.actionFolderDetailFragmentToCreateStudyModuleFragment(
//            folderId = args.folderId
//        )
//        findNavController().navigate(action)
//    }
}