// features/auth/presentation/ui/LoginFragment.kt
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
import com.kardio.databinding.FragmentLoginBinding
import com.kardio.features.auth.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        with(binding) {
            // Thiết lập sự kiện click
            loginButton.setOnClickListener { handleSubmit() }
            registerLink.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            forgotPasswordLink.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collectLatest { state ->
                when (state) {
                    is AuthViewModel.AuthState.Loading -> {
                        binding.loginButton.isLoading = true
                    }
                    is AuthViewModel.AuthState.Success -> {
                        binding.loginButton.isLoading = false
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                    is AuthViewModel.AuthState.Error -> {
                        binding.loginButton.isLoading = false
                        // Hiển thị các lỗi
                        with(binding) {
                            emailTextField.error = state.errors["email"]
                            passwordTextField.error = state.errors["password"]

                            if (state.errors.containsKey("general")) {
                                errorMessage.text = state.errors["general"]
                                errorMessage.visibility = View.VISIBLE
                            } else {
                                errorMessage.visibility = View.GONE
                            }
                        }
                    }
                    else -> {
                        binding.loginButton.isLoading = false
                    }
                }
            }
        }
    }

    private fun handleSubmit() {
        with(binding) {
            viewModel.login(
                emailTextField.getText().trim(),
                passwordTextField.getText()
            )
        }
    }
}