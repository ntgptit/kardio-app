package com.kardio.features.welcome.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kardio.R
import com.kardio.features.welcome.presentation.viewmodel.WelcomeViewModel
import com.kardio.ui.components.buttons.KardioPrimaryButton
import com.kardio.ui.components.buttons.KardioSecondaryButton
import com.kardio.ui.theme.Accent
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.Divider
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.TextSecondary

/**
 * Màn hình chào mừng - chuyển từ fragment_welcome.xml
 */
@Composable
fun WelcomeScreen(
    navigateToRegister: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f))

        // Avatar image
        val avatarImage: Painter = painterResource(id = R.drawable.auth_avatar)
        Image(
            painter = avatarImage,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = Divider, shape = CircleShape)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "Cách tốt nhất để học. Đăng ký miễn phí.",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtitle
        Text(
            text = "Bằng việc đăng ký, bạn chấp nhận Điều khoản dịch vụ và Chính sách quyền riêng tư của Kardio",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Google Login Button
        KardioSecondaryButton(
            text = "Tiếp tục với Google",
            onClick = { viewModel.onGoogleSignInClick(navigateToHome) },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = BackgroundSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Register Button
        KardioPrimaryButton(
            text = "Đăng ký bằng email",
            onClick = { viewModel.onEmailSignUpClick(navigateToRegister) },
            icon = Icons.Outlined.Email,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Login Link
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Đã có tài khoản?",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            TextButton(onClick = { viewModel.onLoginClick(navigateToLogin) }) {
                Text(
                    text = "Đăng nhập",
                    style = MaterialTheme.typography.labelLarge,
                    color = Accent
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))
    }
}