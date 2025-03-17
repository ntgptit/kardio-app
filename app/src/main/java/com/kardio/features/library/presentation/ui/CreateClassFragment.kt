package com.kardio.features.library.presentation.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentCreateClassBinding
import com.kardio.features.library.presentation.viewmodel.CreateClassViewModel
import com.kardio.features.library.presentation.viewmodel.CreateFolderViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateClassFragment : BaseFragment<FragmentCreateClassBinding>() {

    private val viewModel: CreateClassViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateClassBinding {
        return FragmentCreateClassBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupInputFields()
        setupButtons()
        setupWindowInsets()
        observeUiState()
        observeEvents()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_create_class -> {
                    // Xử lý khi người dùng nhấn vào "Tạo class"
//                    Toast.makeText(this, "Tạo class được nhấn!", Toast.LENGTH_SHORT).show()
                    submitClassCreation()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupInputFields() {
        binding.textFieldName.getEditText().setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.clearValidationError()
                binding.scrollView.smoothScrollTo(0, binding.textFieldName.top)
            }
        }

        binding.textFieldDescription.getEditText().setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.scrollView.smoothScrollTo(0, binding.textFieldDescription.top)
            }
        }
    }

    private fun setupButtons() {
//        binding.buttonCreate.setOnClickListener {
//            submitClassCreation()
//        }

        binding.buttonAdvancedSettings.setOnClickListener {
            QlzSnackbar.showInfo(requireContext(), "Advanced settings clicked")
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.updatePadding(top = systemBars.top)
            binding.scrollView.updatePadding(bottom = systemBars.bottom)
            insets
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isCreating
//                    binding.buttonCreate.isEnabled = !state.isCreating
                    binding.textFieldName.isEnabled = !state.isCreating
                    binding.textFieldDescription.isEnabled = !state.isCreating
                    binding.switchAllowMembersToAdd.isEnabled = !state.isCreating

                    binding.textFieldName.error = state.validationError

                    state.error?.let { error ->
                        QlzSnackbar.showError(requireContext(), error)
                        viewModel.errorShown()
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is CreateClassViewModel.CreateClassUiEvent.ClassCreated -> {
                            QlzSnackbar.showSuccess(
                                requireContext(),
                                getString(R.string.message_class_created_success)
                            )
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    private fun submitClassCreation() {
        val className = binding.textFieldName.getText().toString().trim()
        val description = binding.textFieldDescription.getText().toString().trim()
        val allowMembersToAdd = binding.switchAllowMembersToAdd.isChecked

        viewModel.createClass(
            name = className,
            description = description,
            allowMembersToAdd = allowMembersToAdd
        )
    }
}