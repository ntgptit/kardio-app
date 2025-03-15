package com.kardio.ui.theme

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ThemeManager là lớp quản lý theme của ứng dụng
 * bao gồm dark mode, accent colors và các tính năng trợ năng.
 */
@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val adaptiveThemeHelper: AdaptiveThemeHelper,
    private val colorBlindnessHelper: ColorBlindnessHelper
) {
    // Keys for SharedPreferences
    private companion object {
        const val PREFS_NAME = "kardio_theme_prefs"
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_ACCENT_COLOR = "accent_color"
        const val KEY_USE_SYSTEM_ACCENT = "use_system_accent"
        const val KEY_CONTRAST_LEVEL = "contrast_level"
        const val KEY_COLOR_BLINDNESS_TYPE = "color_blindness_type"
        const val KEY_HIGH_CONTRAST_MODE = "high_contrast_mode"
        const val KEY_REDUCED_MOTION = "reduced_motion"
        const val KEY_ADAPTIVE_BRIGHTNESS = "adaptive_brightness"
        const val KEY_LIGHT_SENSOR_THRESHOLD = "light_sensor_threshold"

        // Default values
        val DEFAULT_ACCENT_COLOR = Color.parseColor("#4255FF") // Quizlet blue
        const val DEFAULT_THEME_MODE = AdaptiveThemeHelper.THEME_MODE_SYSTEM
        const val DEFAULT_CONTRAST_LEVEL = 1.0f
        const val DEFAULT_COLOR_BLINDNESS_TYPE = ColorBlindnessHelper.TYPE_NONE
        const val DEFAULT_LIGHT_SENSOR_THRESHOLD = 10 // Lux
    }

    // Shared preferences for persisting theme settings
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    // State flows for reactive UI updates
    private val _themeMode = MutableStateFlow(
        prefs.getInt(KEY_THEME_MODE, DEFAULT_THEME_MODE)
    )
    val themeMode: StateFlow<Int> = _themeMode.asStateFlow()

    private val _accentColor = MutableStateFlow(
        prefs.getInt(KEY_ACCENT_COLOR, DEFAULT_ACCENT_COLOR)
    )
    val accentColor: StateFlow<Int> = _accentColor.asStateFlow()

    private val _useSystemAccent = MutableStateFlow(
        prefs.getBoolean(KEY_USE_SYSTEM_ACCENT, false)
    )
    val useSystemAccent: StateFlow<Boolean> = _useSystemAccent.asStateFlow()

    private val _contrastLevel = MutableStateFlow(
        prefs.getFloat(KEY_CONTRAST_LEVEL, DEFAULT_CONTRAST_LEVEL)
    )
    val contrastLevel: StateFlow<Float> = _contrastLevel.asStateFlow()

    private val _colorBlindnessType = MutableStateFlow(
        prefs.getInt(KEY_COLOR_BLINDNESS_TYPE, DEFAULT_COLOR_BLINDNESS_TYPE)
    )
    val colorBlindnessType: StateFlow<Int> = _colorBlindnessType.asStateFlow()

    private val _highContrastMode = MutableStateFlow(
        prefs.getBoolean(KEY_HIGH_CONTRAST_MODE, false)
    )
    val highContrastMode: StateFlow<Boolean> = _highContrastMode.asStateFlow()

    private val _reducedMotion = MutableStateFlow(
        prefs.getBoolean(KEY_REDUCED_MOTION, false)
    )
    val reducedMotion: StateFlow<Boolean> = _reducedMotion.asStateFlow()

    private val _adaptiveBrightness = MutableStateFlow(
        prefs.getBoolean(KEY_ADAPTIVE_BRIGHTNESS, false)
    )
    val adaptiveBrightness: StateFlow<Boolean> = _adaptiveBrightness.asStateFlow()

    private val _lightSensorThreshold = MutableStateFlow(
        prefs.getInt(KEY_LIGHT_SENSOR_THRESHOLD, DEFAULT_LIGHT_SENSOR_THRESHOLD)
    )
    val lightSensorThreshold: StateFlow<Int> = _lightSensorThreshold.asStateFlow()

    // Initialize with saved preferences
    init {
        // Apply saved settings to helpers
        applyThemeMode(_themeMode.value)
        applyContrastLevel(_contrastLevel.value)
        applyColorBlindnessSettings(_colorBlindnessType.value, _highContrastMode.value)
        applyAdaptiveBrightness(_adaptiveBrightness.value, _lightSensorThreshold.value)
    }

    /**
     * Set the theme mode (system, light, dark, adaptive)
     */
    fun setThemeMode(mode: Int) {
        _themeMode.value = mode
        prefs.edit { putInt(KEY_THEME_MODE, mode) }
        applyThemeMode(mode)
    }

    /**
     * Apply the selected theme mode
     */
    private fun applyThemeMode(mode: Int) {
        adaptiveThemeHelper.setThemeMode(mode)
    }

    /**
     * Set accent color for the app
     */
    fun setAccentColor(@ColorInt color: Int) {
        _accentColor.value = color
        prefs.edit { putInt(KEY_ACCENT_COLOR, color) }

        // If system accent is enabled, disable it when manually setting accent
        if (_useSystemAccent.value) {
            setUseSystemAccent(false)
        }
    }

    /**
     * Get the active accent color, which might be adapted for color blindness
     */
    @ColorInt
    fun getActiveAccentColor(): Int {
        val baseColor = _accentColor.value
        return colorBlindnessHelper.adaptColor(baseColor)
    }

    /**
     * Set whether to use system accent color
     */
    fun setUseSystemAccent(use: Boolean) {
        _useSystemAccent.value = use
        prefs.edit { putBoolean(KEY_USE_SYSTEM_ACCENT, use) }

        if (use) {
            // Extract system accent color and update
            val systemAccent = getSystemAccentColor()
            _accentColor.value = systemAccent
            prefs.edit { putInt(KEY_ACCENT_COLOR, systemAccent) }
        }
    }

    /**
     * Get system accent color
     */
    @ColorInt
    private fun getSystemAccentColor(): Int {
        // Extract system accent color if possible
        return try {
            val typedValue = android.util.TypedValue()
            context.theme.resolveAttribute(
                android.R.attr.colorAccent, typedValue, true
            )
            typedValue.data
        } catch (e: Exception) {
            DEFAULT_ACCENT_COLOR
        }
    }

    /**
     * Set contrast level for better readability
     */
    fun setContrastLevel(level: Float) {
        val clampedLevel = level.coerceIn(0.8f, 1.2f)
        _contrastLevel.value = clampedLevel
        prefs.edit { putFloat(KEY_CONTRAST_LEVEL, clampedLevel) }
        applyContrastLevel(clampedLevel)
    }

    /**
     * Apply contrast level to adaptive theme helper
     */
    private fun applyContrastLevel(level: Float) {
        adaptiveThemeHelper.setContrastFactor(level)
    }

    /**
     * Set color blindness type
     */
    fun setColorBlindnessType(type: Int) {
        _colorBlindnessType.value = type
        prefs.edit { putInt(KEY_COLOR_BLINDNESS_TYPE, type) }
        applyColorBlindnessSettings(type, _highContrastMode.value)
    }

    /**
     * Set high contrast mode for accessibility
     */
    fun setHighContrastMode(enabled: Boolean) {
        _highContrastMode.value = enabled
        prefs.edit { putBoolean(KEY_HIGH_CONTRAST_MODE, enabled) }
        applyColorBlindnessSettings(_colorBlindnessType.value, enabled)
    }

    /**
     * Apply color blindness settings to helper
     */
    private fun applyColorBlindnessSettings(type: Int, highContrast: Boolean) {
        colorBlindnessHelper.setColorBlindnessType(type)
        colorBlindnessHelper.setHighContrastMode(highContrast)
    }

    /**
     * Set reduced motion preference for animations
     */
    fun setReducedMotion(enabled: Boolean) {
        _reducedMotion.value = enabled
        prefs.edit { putBoolean(KEY_REDUCED_MOTION, enabled) }
    }

    /**
     * Check if reduced motion should be used
     */
    fun shouldUseReducedMotion(): Boolean {
        // Chỉ kiểm tra cài đặt người dùng
        return _reducedMotion.value
    }

    /**
     * Set adaptive brightness mode
     */
    fun setAdaptiveBrightness(enabled: Boolean) {
        _adaptiveBrightness.value = enabled
        prefs.edit { putBoolean(KEY_ADAPTIVE_BRIGHTNESS, enabled) }
        applyAdaptiveBrightness(enabled, _lightSensorThreshold.value)
    }

    /**
     * Set light sensor threshold for adaptive brightness
     */
    fun setLightSensorThreshold(threshold: Int) {
        _lightSensorThreshold.value = threshold
        prefs.edit { putInt(KEY_LIGHT_SENSOR_THRESHOLD, threshold) }
        applyAdaptiveBrightness(_adaptiveBrightness.value, threshold)
    }

    /**
     * Apply adaptive brightness settings
     */
    private fun applyAdaptiveBrightness(enabled: Boolean, threshold: Int) {
        if (enabled && adaptiveThemeHelper.isLightSensorAvailable()) {
            adaptiveThemeHelper.setLightSensorThreshold(threshold)
            if (_themeMode.value != AdaptiveThemeHelper.THEME_MODE_AUTO_LIGHT_SENSOR) {
                setThemeMode(AdaptiveThemeHelper.THEME_MODE_AUTO_LIGHT_SENSOR)
            }
        } else if (_themeMode.value == AdaptiveThemeHelper.THEME_MODE_AUTO_LIGHT_SENSOR) {
            // Switch to system theme if light sensor was being used
            setThemeMode(AdaptiveThemeHelper.THEME_MODE_SYSTEM)
        }
    }

    /**
     * Check if a dark theme is currently active
     */
    fun isDarkThemeActive(): Boolean {
        return adaptiveThemeHelper.isDarkMode.value == true
    }

    /**
     * Apply color adjustments for current settings (color blindness, contrast)
     * @param color The original color
     * @return The adjusted color
     */
    @ColorInt
    fun adjustColor(@ColorInt color: Int): Int {
        // First adjust for color blindness if needed
        var adjustedColor = colorBlindnessHelper.adaptColor(color)

        // Then adjust contrast if needed
        if (_contrastLevel.value != 1.0f) {
            adjustedColor = adaptiveThemeHelper.adjustContrast(adjustedColor)
        }

        return adjustedColor
    }

    /**
     * Get a color palette that is safe for the current accessibility settings
     */
    fun getSafeColorPalette(): List<Int> {
        return if (_colorBlindnessType.value != ColorBlindnessHelper.TYPE_NONE) {
            colorBlindnessHelper.getSafeColorPalette()
        } else {
            // Default color palette từ Quizlet
            listOf(
                Color.parseColor("#4255FF"),  // Primary blue
                Color.parseColor("#FF9B27"),  // Orange (streak flame)
                Color.parseColor("#2EACDC"),  // Light blue (flashcard)
                Color.parseColor("#767890"),  // Grey
                Color.parseColor("#4CAF50")   // Success green
            )
        }
    }

    /**
     * Cleanup resources when the application is being destroyed
     */
    fun cleanup() {
        adaptiveThemeHelper.cleanup()
    }
}