// features/auth/presentation/ui/RegisterFragment.kt
package com.kardio.features.auth.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentRegisterBinding
import com.kardio.features.auth.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        with(binding) {
            // Thiết lập sự kiện click
            registerButton.setOnClickListener { handleSubmit() }
            loginLink.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collectLatest { state ->
                when (state) {
                    is AuthViewModel.AuthState.Loading -> {
                        binding.registerButton.isLoading = true
                    }
                    is AuthViewModel.AuthState.Success -> {
                        binding.registerButton.isLoading = false
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    }
                    is AuthViewModel.AuthState.Error -> {
                        binding.registerButton.isLoading = false
                        // Hiển thị các lỗi
                        with(binding) {
                            displayNameTextField.error = state.errors["displayName"]
                            emailTextField.error = state.errors["email"]
                            passwordTextField.error = state.errors["password"]
                            confirmPasswordTextField.error = state.errors["confirmPassword"]

                            if (state.errors.containsKey("general")) {
                                errorMessage.text = state.errors["general"]
                                errorMessage.visibility = View.VISIBLE
                            } else {
                                errorMessage.visibility = View.GONE
                            }
                        }
                    }
                    else -> {
                        binding.registerButton.isLoading = false
                    }
                }
            }
        }
    }

    private fun handleSubmit() {
        with(binding) {
            viewModel.register(
                displayNameTextField.getText().trim(),
                emailTextField.getText().trim(),
                passwordTextField.getText(),
                confirmPasswordTextField.getText()
            )
        }
    }
}