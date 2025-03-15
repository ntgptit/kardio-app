package com.kardio.ui.theme

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper class for adaptive theme management.
 * This class provides functionality to:
 * 1. Automatically adjust between light and dark mode based on system settings
 * 2. Adjust theme based on ambient light sensor
 * 3. Dynamically adjust contrast levels for better readability
 */
@Singleton
class AdaptiveThemeHelper @Inject constructor(
    @ApplicationContext private val context: Context
) : SensorEventListener {

    companion object {
        // Constants for ambient light thresholds (lux)
        private const val LIGHT_THRESHOLD_DARK = 10    // Below this, force dark mode
        private const val LIGHT_THRESHOLD_LIGHT = 500  // Above this, force light mode if user has auto

        // Contrast adjustment constants
        private const val MIN_CONTRAST_FACTOR = 0.8f
        private const val MAX_CONTRAST_FACTOR = 1.2f

        // Theme modes
        const val THEME_MODE_SYSTEM = 0
        const val THEME_MODE_LIGHT = 1
        const val THEME_MODE_DARK = 2
        const val THEME_MODE_AUTO_LIGHT_SENSOR = 3
    }

    // Sensor manager for light sensor
    private val sensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val lightSensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    // Current theme settings
    private var currentThemeMode = THEME_MODE_SYSTEM
    private var lightSensorEnabled = false
    private var lightSensorThreshold = LIGHT_THRESHOLD_DARK
    private var contrastFactor = 1.0f

    // LiveData for theme state
    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> get() = _isDarkMode

    private val _contrastLevel = MutableLiveData<Float>()
    val contrastLevel: LiveData<Float> get() = _contrastLevel

    init {
        // Initialize with current system values
        updateDarkModeState()
    }

    /**
     * Set theme mode
     * @param mode Theme mode to set (THEME_MODE_*)
     */
    fun setThemeMode(mode: Int) {
        currentThemeMode = mode

        when (mode) {
            THEME_MODE_SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                stopLightSensor()
            }
            THEME_MODE_LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                stopLightSensor()
            }
            THEME_MODE_DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                stopLightSensor()
            }
            THEME_MODE_AUTO_LIGHT_SENSOR -> {
                startLightSensor()
            }
        }

        updateDarkModeState()
    }

    /**
     * Set contrast factor for the theme
     * @param factor Contrast factor (0.8 to 1.2)
     */
    fun setContrastFactor(factor: Float) {
        contrastFactor = factor.coerceIn(MIN_CONTRAST_FACTOR, MAX_CONTRAST_FACTOR)
        _contrastLevel.value = contrastFactor
    }

    /**
     * Set light sensor threshold for dark mode
     * @param threshold Light threshold in lux
     */
    fun setLightSensorThreshold(threshold: Int) {
        lightSensorThreshold = threshold

        // If light sensor is active, update theme based on new threshold
        if (lightSensorEnabled && currentThemeMode == THEME_MODE_AUTO_LIGHT_SENSOR) {
            updateThemeBasedOnLightSensor(lastLightValue)
        }
    }

    /**
     * Start listening to light sensor changes
     */
    private fun startLightSensor() {
        lightSensor?.let {
            lightSensorEnabled = true
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    /**
     * Stop listening to light sensor changes
     */
    private fun stopLightSensor() {
        if (lightSensorEnabled) {
            sensorManager.unregisterListener(this)
            lightSensorEnabled = false
        }
    }

    /**
     * Update the dark mode state based on the current configuration
     */
    private fun updateDarkModeState() {
        val isDark = when (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }

        _isDarkMode.value = isDark
    }

    /**
     * Store the last light sensor value
     */
    private var lastLightValue = -1f

    /**
     * Update theme based on light sensor reading
     */
    private fun updateThemeBasedOnLightSensor(lightValue: Float) {
        lastLightValue = lightValue

        if (!lightSensorEnabled || currentThemeMode != THEME_MODE_AUTO_LIGHT_SENSOR) {
            return
        }

        // Adjust night mode based on light level
        when {
            lightValue < lightSensorThreshold -> {
                if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    updateDarkModeState()
                }
            }
            lightValue > LIGHT_THRESHOLD_LIGHT -> {
                if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    updateDarkModeState()
                }
            }
        }

        // Also adjust contrast based on light level
        // Smoothly transition contrast between ranges
        val normalizedLight = (lightValue / LIGHT_THRESHOLD_LIGHT).coerceIn(0f, 1f)
        val newContrastFactor = MIN_CONTRAST_FACTOR + normalizedLight * (MAX_CONTRAST_FACTOR - MIN_CONTRAST_FACTOR)

        if (Math.abs(newContrastFactor - contrastFactor) > 0.05f) {  // Only update if change is significant
            setContrastFactor(newContrastFactor)
        }
    }

    /**
     * Adjust color contrast based on current contrast factor
     * @param color The original color
     * @return The contrast-adjusted color
     */
    fun adjustContrast(@androidx.annotation.ColorInt color: Int): Int {
        if (contrastFactor == 1.0f) {
            return color  // No adjustment needed
        }

        // Extract color components
        val a = Color.alpha(color)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)

        // For dark mode, we increase contrast by making darks darker and lights lighter
        val isDark = _isDarkMode.value == true

        return if (isDark) {
            // In dark mode
            if (isLightColor(color)) {
                // Make light colors lighter
                val adjustedR = (r + (255 - r) * (contrastFactor - 1.0f)).toInt().coerceIn(0, 255)
                val adjustedG = (g + (255 - g) * (contrastFactor - 1.0f)).toInt().coerceIn(0, 255)
                val adjustedB = (b + (255 - b) * (contrastFactor - 1.0f)).toInt().coerceIn(0, 255)
                Color.argb(a, adjustedR, adjustedG, adjustedB)
            } else {
                // Make dark colors darker
                val adjustedR = (r * contrastFactor).toInt().coerceIn(0, 255)
                val adjustedG = (g * contrastFactor).toInt().coerceIn(0, 255)
                val adjustedB = (b * contrastFactor).toInt().coerceIn(0, 255)
                Color.argb(a, adjustedR, adjustedG, adjustedB)
            }
        } else {
            // In light mode
            if (isLightColor(color)) {
                // Make light colors even lighter
                val adjustedR = (r * contrastFactor).toInt().coerceIn(0, 255)
                val adjustedG = (g * contrastFactor).toInt().coerceIn(0, 255)
                val adjustedB = (b * contrastFactor).toInt().coerceIn(0, 255)
                Color.argb(a, adjustedR, adjustedG, adjustedB)
            } else {
                // Make dark colors darker
                val adjustedR = (r - r * (contrastFactor - 1.0f)).toInt().coerceIn(0, 255)
                val adjustedG = (g - g * (contrastFactor - 1.0f)).toInt().coerceIn(0, 255)
                val adjustedB = (b - b * (contrastFactor - 1.0f)).toInt().coerceIn(0, 255)
                Color.argb(a, adjustedR, adjustedG, adjustedB)
            }
        }
    }

    /**
     * Check if a color is considered "light"
     * @param color The color to check
     * @return True if the color is light, false otherwise
     */
    private fun isLightColor(@androidx.annotation.ColorInt color: Int): Boolean {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)

        // Calculate perceived brightness using the formula:
        // brightness = 0.299R + 0.587G + 0.114B
        val brightness = (0.299 * r + 0.587 * g + 0.114 * b).toInt()

        // Consider colors with brightness > 127 as "light"
        return brightness > 127
    }

    // SensorEventListener implementation

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            val lightValue = event.values[0]
            updateThemeBasedOnLightSensor(lightValue)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Not needed for our use case
    }

    /**
     * Check if the device supports light sensor
     * @return True if light sensor is available, false otherwise
     */
    fun isLightSensorAvailable(): Boolean {
        return lightSensor != null
    }

    /**
     * Cleanup when the application is closing
     */
    fun cleanup() {
        stopLightSensor()
    }
}