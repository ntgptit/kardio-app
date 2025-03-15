// features/auth/presentation/ui/ForgotPasswordFragment.kt
package com.kardio.features.auth.presentation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentForgotPasswordBinding
import com.kardio.features.auth.presentation.viewmodel.AuthViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentForgotPasswordBinding {
        return FragmentForgotPasswordBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        binding.resetPasswordButton.setOnClickListener { handleSubmit() }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collectLatest { state ->
                when (state) {
                    is AuthViewModel.AuthState.Loading -> {
                        binding.resetPasswordButton.isLoading = true
                    }
                    is AuthViewModel.AuthState.Success -> {
                        binding.resetPasswordButton.isLoading = false
                        QlzSnackbar.showSuccess(
                            requireContext(),
                            "Link đặt lại mật khẩu đã được gửi đến email của bạn"
                        )
                        findNavController().popBackStack()
                    }
                    is AuthViewModel.AuthState.Error -> {
                        binding.resetPasswordButton.isLoading = false
                        // Hiển thị các lỗi
                        with(binding) {
                            emailTextField.error = state.errors["email"]

                            if (state.errors.containsKey("general")) {
                                errorMessage.text = state.errors["general"]
                                errorMessage.visibility = View.VISIBLE
                            } else {
                                errorMessage.visibility = View.GONE
                            }
                        }
                    }
                    else -> {
                        binding.resetPasswordButton.isLoading = false
                    }
                }
            }
        }
    }

    private fun handleSubmit() {
        viewModel.forgotPassword(binding.emailTextField.getText().trim())
    }
}