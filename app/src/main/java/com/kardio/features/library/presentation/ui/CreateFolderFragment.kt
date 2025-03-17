package com.kardio.features.library.presentation.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentCreateFolderBinding
import com.kardio.features.library.presentation.viewmodel.CreateFolderViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateFolderFragment : BaseFragment<FragmentCreateFolderBinding>() {

    private val viewModel: CreateFolderViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateFolderBinding {
        return FragmentCreateFolderBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupInputField()
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
                R.id.action_create_folder -> {
                    // Xử lý khi người dùng nhấn vào "Tạo class"
                    submitFolderCreation()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupInputField() {
        binding.textFieldName.getEditText().setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.clearValidationError()
                // Cuộn mượt mà đến vị trí text field
                binding.scrollView.smoothScrollTo(0, binding.textFieldName.top)
            }
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
                    binding.textFieldName.isEnabled = !state.isCreating
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
                        is CreateFolderViewModel.CreateFolderUiEvent.FolderCreated -> {
                            QlzSnackbar.showSuccess(
                                requireContext(),
                                getString(R.string.message_folder_created_success)
                            )
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    private fun submitFolderCreation() {
        val folderName = binding.textFieldName.getText().toString().trim()
        viewModel.createFolder(
            name = folderName,
            description = ""
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_folder, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_create_folder -> {
                submitFolderCreation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}