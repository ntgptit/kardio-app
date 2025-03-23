package com.kardio.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// Màu sắc được chuyển từ colors.xml
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Grey = Color(0xFFB0B0B0)
val Transparent = Color(0x00000000)

val Primary = Color(0xFF4255FF)
val Accent = Color(0xFF4255FF)
val Secondary = Color(0xFF2196F3)

val BackgroundPrimary = Color(0xFF0F1429)
val BackgroundSecondary = Color(0xFF1E2642)
val BackgroundInput = Color(0xFF232A4B)
val Surface = Color(0xFF1E2642)
val DarkSurface = Color(0xFF1E2642)

val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB0B4C6)
val TextTertiary = Color(0xFF72778F)

val PrimaryButton = Color(0xFF4255FF)
val SecondaryButtonStroke = Color(0xFFFFFFFF)
val SecondaryButtonBackground = Color(0xFF1A1A2E)

val Divider = Color(0xFF2A2E43)

val NavItemActive = Color(0xFFFFFFFF)
val NavItemInactive = Color(0xFF72778F)
val TabSelected = Color(0xFFFFFFFF)
val TabUnselected = Color(0xFF72778F)
val TabIndicator = Color(0xFF4255FF)

val Success = Color(0xFF4CAF50)
val Error = Color(0xFFF44336)
val Warning = Color(0xFFFF9800)

val IconPrimary = Color(0xFFFFFFFF)
val IconSecondary = Color(0xFF72778F)
val IconBlue = Color(0xFF2196F3)
val IconGreen = Color(0xFF4CAF50)
val IconPurple = Color(0xFF9C27B0)
val IconOrange = Color(0xFFFF9800)

val BadgePlus = Color(0xFFFFFFFF)
val BadgeBackground = Color(0xFF767890)

val PrimaryAlpha20 = Color(0x334255FF)
val SurfaceAlpha90 = Color(0xE61E2642)
val Scrim = Color(0x80000000)

val StreakFlameOrange = Color(0xFFFF9B27)
val StreakFlameYellow = Color(0xFFFFCB47)

val FlashcardIcon = Color(0xFF2EACDC)
val FlashcardFrontBg = Color(0xFF1E2642)
val FlashcardBackBg = Color(0xFF232A4B)

// Dark theme colors (hiện tại là dark mode, giống với màu ở trên)
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = TextPrimary,
    secondary = Secondary,
    onSecondary = TextPrimary,
    background = BackgroundPrimary,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    error = Error,
    onError = TextPrimary
)

// Light theme colors (cần tạo riêng cho light mode)
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = White,
    secondary = Secondary,
    onSecondary = White,
    background = White,
    onBackground = Black,
    surface = Color(0xFFF5F5F5),
    onSurface = Black,
    error = Error,
    onError = White
)

// Typography được chuyển từ typography.xml
@Composable
fun KardioTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Thiết lập màu sắc cho system bars
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme

    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
            isNavigationBarContrastEnforced = false
        )
        onDispose {}
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Extension nắm giữ các màu sắc tùy chỉnh không có trong Material Theme
object KardioColors {
    val textTertiary: Color = TextTertiary
    val divider: Color = Divider
    val backgroundSecondary: Color = BackgroundSecondary
    val backgroundInput: Color = BackgroundInput
    val navItemActive: Color = NavItemActive
    val navItemInactive: Color = NavItemInactive
    val tabSelected: Color = TabSelected
    val tabUnselected: Color = TabUnselected
    val tabIndicator: Color = TabIndicator
    val iconPrimary: Color = IconPrimary
    val iconSecondary: Color = IconSecondary
    val streakFlameOrange: Color = StreakFlameOrange
    val flashcardFrontBg: Color = FlashcardFrontBg
    val flashcardBackBg: Color = FlashcardBackBg
}

// Spacing values từ dimens.xml
object Spacing {
    val XXS = 2
    val XS = 4
    val SM = 8
    val MD = 16
    val LG = 24
    val XL = 32
    val XXL = 48
    val XXXL = 64
}

// Radius values từ dimens.xml
object Radius {
    val None = 0
    val XS = 4
    val SM = 8
    val MD = 16
    val LG = 24
    val XL = 32
    val Circle = 1000
}