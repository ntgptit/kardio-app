// File: app/src/main/java/com/kardio/features/library/presentation/ui/ClassDetailFragment.kt
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
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentClassDetailBinding
import com.kardio.features.library.presentation.viewmodel.ClassDetailViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClassDetailFragment : BaseFragment<FragmentClassDetailBinding>() {

    private val viewModel: ClassDetailViewModel by viewModels()
    private val args: ClassDetailFragmentArgs by navArgs()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentClassDetailBinding {
        return FragmentClassDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadClassData()
    }

    private fun loadClassData() {
        viewModel.loadClassDetail(args.classId)
    }

    override fun setupViews() {
        // Set class name in toolbar
        binding.toolbar.title = args.className

        // Setup back button
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Setup "Join Class" button
        binding.buttonJoinClass.setOnClickListener {
            viewModel.joinClass()
        }

        // Setup "Add Study Module" button
        binding.buttonAddStudyModule.setOnClickListener {
            navigateToCreateStudyModule()
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

    private fun updateUi(state: ClassDetailViewModel.ClassDetailUiState) {
        // Update loading state
        binding.progressBar.isVisible = state.isLoading

        // Update main content visibility
        binding.layoutContent.isVisible = !state.isLoading && state.error == null && state.classDetail != null

        // Update error state
        binding.layoutErrorState.root.isVisible = state.error != null && !state.isLoading

        if (state.error != null && !state.error.isNullOrEmpty()) {
            binding.layoutErrorState.textViewErrorMessage.text = state.error
            QlzSnackbar.showError(requireContext(), state.error)
            viewModel.errorShown()
        }

        // Update UI with class details
        state.classDetail?.let { classDetail ->
            binding.textViewClassName.text = classDetail.name
            binding.textViewClassDescription.text = classDetail.description.takeIf { it.isNotEmpty() }
                ?: "Lớp học chứa ${classDetail.studyModulesCount} học phần"
            binding.textViewMembersCount.text = "${classDetail.membersCount} thành viên"
            binding.textViewModulesCount.text = "${classDetail.studyModulesCount} học phần"

            // Show creator information
            if (classDetail.creatorName != null && classDetail.creatorName.isNotEmpty()) {
                binding.textViewCreator.text = "Tạo bởi: ${classDetail.creatorName}"
                binding.textViewCreator.isVisible = true
            } else {
                binding.textViewCreator.isVisible = false
            }

            // Update permission indicators
            binding.chipPermission.isVisible = classDetail.allowMembersToAdd

            // Update join status
            binding.buttonJoinClass.isVisible = !classDetail.hasJoined
            binding.buttonLeaveClass.isVisible = classDetail.hasJoined

            // Update "Add Study Module" button visibility based on permissions
            binding.buttonAddStudyModule.isVisible = classDetail.hasJoined &&
                    (classDetail.isCreatedByCurrentUser || classDetail.allowMembersToAdd)
        }

        // Show any action messages
        if (state.actionMessage != null && state.actionMessage.isNotEmpty()) {
            QlzSnackbar.showInfo(requireContext(), state.actionMessage)
            viewModel.actionMessageShown()
        }
    }

    private fun navigateToCreateStudyModule() {
        // Example navigation - adjust according to your nav graph
        val action = ClassDetailFragmentDirections.actionClassDetailFragmentToCreateStudyModuleFragment(
            classId = args.classId
        )
        findNavController().navigate(action)
    }
}