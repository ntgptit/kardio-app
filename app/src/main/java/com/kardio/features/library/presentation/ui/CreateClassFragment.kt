// File: app/src/main/java/com/kardio/features/library/presentation/ui/CreateClassFragment.kt
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
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentCreateClassBinding
import com.kardio.features.library.presentation.viewmodel.CreateClassViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateClassFragment : BaseFragment<FragmentCreateClassBinding>() {

    private val viewModel: CreateClassViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCreateClassBinding {
        return FragmentCreateClassBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        // Setup toolbar
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Setup create button
        binding.buttonCreate.setOnClickListener {
            createClass()
        }

        // Setup text field listeners
        binding.textFieldName.getEditText().setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.clearValidationError()
            }
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

    private fun updateUi(state: CreateClassViewModel.CreateClassUiState) {
        // Update loading state
        binding.progressBar.isVisible = state.isCreating
        binding.buttonCreate.isEnabled = !state.isCreating

        // Handle validation error
        if (state.validationError != null) {
            binding.textFieldName.error = state.validationError
        } else {
            binding.textFieldName.error = null
        }

        // Handle error
        if (state.error != null) {
            QlzSnackbar.showError(requireContext(), state.error)
            viewModel.errorShown()
        }
    }

    private fun createClass() {
        val name = binding.textFieldName.getText().toString().trim()
        val description = binding.textFieldDescription.getText().toString().trim()
        val allowMembersToAdd = binding.switchAllowMembersToAdd.isChecked

        viewModel.createClass(name, description, allowMembersToAdd)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe one-time events
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is CreateClassViewModel.CreateClassUiEvent.ClassCreated -> {
                            QlzSnackbar.showSuccess(requireContext(),
                                getString(R.string.message_class_created_success))
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }
}