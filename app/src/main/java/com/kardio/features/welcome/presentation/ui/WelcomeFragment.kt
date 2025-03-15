// features/welcome/presentation/ui/WelcomeFragment.kt
package com.kardio.features.welcome.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentWelcomeBinding
import com.kardio.features.welcome.presentation.viewmodel.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {

    private val viewModel: WelcomeViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentWelcomeBinding {
        return FragmentWelcomeBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        // Kiểm tra kích thước màn hình và điều chỉnh UI phù hợp
        val isSmallScreen = resources.getBoolean(R.bool.is_small_screen)

        with(binding) {
            // Cấu hình avatar
            val avatarSize = if (isSmallScreen)
                resources.getDimensionPixelSize(R.dimen.avatar_size_small)
            else
                resources.getDimensionPixelSize(R.dimen.avatar_size_normal)

            profileAvatar.layoutParams.apply {
                width = avatarSize
                height = avatarSize
            }

            // Thiết lập các sự kiện click
            googleButton.setOnClickListener { viewModel.onGoogleSignInClick() }
            emailButton.setOnClickListener { viewModel.onEmailSignUpClick() }
            loginButton.setOnClickListener { viewModel.onLoginClick() }
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigationEvent.collectLatest { event ->
                when (event) {
                    is WelcomeViewModel.NavigationEvent.NavigateToHome ->
                        findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
                    is WelcomeViewModel.NavigationEvent.NavigateToRegister ->
                        findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
                    is WelcomeViewModel.NavigationEvent.NavigateToLogin ->
                        findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
                }
            }
        }
    }
}