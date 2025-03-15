package com.kardio.ui.theme

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min

/**
 * Helper class for supporting color blindness modes.
 * This class provides color transformations for different types of color blindness,
 * ensuring the app is accessible to users with these vision conditions.
 */
@Singleton
class ColorBlindnessHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        // Color blindness types
        const val TYPE_NONE = 0
        const val TYPE_PROTANOPIA = 1  // Red-blindness
        const val TYPE_DEUTERANOPIA = 2  // Green-blindness
        const val TYPE_TRITANOPIA = 3  // Blue-blindness

        // Constants for high contrast mode
        private const val HIGH_CONTRAST_THRESHOLD = 4.5f  // WCAG AA standard for normal text
    }

    // Current settings
    private var colorBlindnessType = TYPE_NONE
    private var useHighContrast = false

    /**
     * Set the color blindness type
     * @param type The type of color blindness to simulate/compensate for
     */
    fun setColorBlindnessType(type: Int) {
        colorBlindnessType = type
    }

    /**
     * Enable or disable high contrast mode
     * @param enabled Whether high contrast mode should be enabled
     */
    fun setHighContrastMode(enabled: Boolean) {
        useHighContrast = enabled
    }

    /**
     * Adapt a color for the current color blindness settings
     * @param color The original color
     * @return The adapted color
     */
    @ColorInt
    fun adaptColor(@ColorInt color: Int): Int {
        // First adapt for color blindness
        var adaptedColor = when (colorBlindnessType) {
            TYPE_PROTANOPIA -> adaptForProtanopia(color)
            TYPE_DEUTERANOPIA -> adaptForDeuteranopia(color)
            TYPE_TRITANOPIA -> adaptForTritanopia(color)
            else -> color
        }

        // Then adjust for high contrast if needed
        if (useHighContrast) {
            adaptedColor = ensureHighContrast(adaptedColor)
        }

        return adaptedColor
    }

    /**
     * Adapt a color for Protanopia (red-blindness)
     * @param color The original color
     * @return The adapted color
     */
    @ColorInt
    private fun adaptForProtanopia(@ColorInt color: Int): Int {
        // Extract RGB components
        val r = Color.red(color) / 255f
        val g = Color.green(color) / 255f
        val b = Color.blue(color) / 255f

        // Protanopia simulation matrix
        val newR = 0.567f * r + 0.433f * g + 0.0f * b
        val newG = 0.558f * r + 0.442f * g + 0.0f * b
        val newB = 0.0f * r + 0.242f * g + 0.758f * b

        // Convert back to 0-255 range and create color
        return Color.argb(
            Color.alpha(color),
            (newR * 255).toInt().coerceIn(0, 255),
            (newG * 255).toInt().coerceIn(0, 255),
            (newB * 255).toInt().coerceIn(0, 255)
        )
    }

    /**
     * Adapt a color for Deuteranopia (green-blindness)
     * @param color The original color
     * @return The adapted color
     */
    @ColorInt
    private fun adaptForDeuteranopia(@ColorInt color: Int): Int {
        // Extract RGB components
        val r = Color.red(color) / 255f
        val g = Color.green(color) / 255f
        val b = Color.blue(color) / 255f

        // Deuteranopia simulation matrix
        val newR = 0.625f * r + 0.375f * g + 0.0f * b
        val newG = 0.7f * r + 0.3f * g + 0.0f * b
        val newB = 0.0f * r + 0.3f * g + 0.7f * b

        // Convert back to 0-255 range and create color
        return Color.argb(
            Color.alpha(color),
            (newR * 255).toInt().coerceIn(0, 255),
            (newG * 255).toInt().coerceIn(0, 255),
            (newB * 255).toInt().coerceIn(0, 255)
        )
    }

    /**
     * Adapt a color for Tritanopia (blue-blindness)
     * @param color The original color
     * @return The adapted color
     */
    @ColorInt
    private fun adaptForTritanopia(@ColorInt color: Int): Int {
        // Extract RGB components
        val r = Color.red(color) / 255f
        val g = Color.green(color) / 255f
        val b = Color.blue(color) / 255f

        // Tritanopia simulation matrix
        val newR = 0.95f * r + 0.05f * g + 0.0f * b
        val newG = 0.0f * r + 0.433f * g + 0.567f * b
        val newB = 0.0f * r + 0.475f * g + 0.525f * b

        // Convert back to 0-255 range and create color
        return Color.argb(
            Color.alpha(color),
            (newR * 255).toInt().coerceIn(0, 255),
            (newG * 255).toInt().coerceIn(0, 255),
            (newB * 255).toInt().coerceIn(0, 255)
        )
    }

    /**
     * Ensure a color has high contrast against the background
     * @param color The original color
     * @return A high contrast version of the color
     */
    @ColorInt
    private fun ensureHighContrast(@ColorInt color: Int): Int {
        // Get system background color (either black or white for simplicity)
        val isDarkMode = context.resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES

        val backgroundColor = if (isDarkMode) Color.BLACK else Color.WHITE

        // Check if contrast is already sufficient
        if (calculateContrast(color, backgroundColor) >= HIGH_CONTRAST_THRESHOLD) {
            return color
        }

        // If not enough contrast, adjust the color
        // In dark mode, we make colors lighter; in light mode, we make them darker
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        val a = Color.alpha(color)

        // Calculate relative luminance
        val lum = calculateRelativeLuminance(color)

        // Target luminance to achieve sufficient contrast
        val targetLum = if (isDarkMode) {
            // In dark mode, increase luminance
            min(lum * 2.0, 1.0)
        } else {
            // In light mode, decrease luminance
            max(lum * 0.5, 0.0)
        }

        // Simple adjustment - this could be more sophisticated
        val factor = (targetLum / lum).toFloat()

        val newR = (r * factor).toInt().coerceIn(0, 255)
        val newG = (g * factor).toInt().coerceIn(0, 255)
        val newB = (b * factor).toInt().coerceIn(0, 255)

        return Color.argb(a, newR, newG, newB)
    }

    /**
     * Calculate the contrast ratio between two colors
     * @param color1 First color
     * @param color2 Second color
     * @return Contrast ratio (1-21)
     */
    private fun calculateContrast(@ColorInt color1: Int, @ColorInt color2: Int): Float {
        val lum1 = calculateRelativeLuminance(color1)
        val lum2 = calculateRelativeLuminance(color2)

        // Ensure lighter color is first for consistent formula
        val lighter = max(lum1, lum2)
        val darker = min(lum1, lum2)

        // Contrast ratio formula: (L1 + 0.05) / (L2 + 0.05)
        return ((lighter + 0.05) / (darker + 0.05)).toFloat()
    }

    /**
     * Calculate the relative luminance of a color
     * @param color The color
     * @return Relative luminance (0-1)
     */
    private fun calculateRelativeLuminance(@ColorInt color: Int): Double {
        // Convert RGB to sRGB
        val r = Color.red(color) / 255.0
        val g = Color.green(color) / 255.0
        val b = Color.blue(color) / 255.0

        // Apply gamma correction
        val rLinear = if (r <= 0.04045) r / 12.92 else Math.pow((r + 0.055) / 1.055, 2.4)
        val gLinear = if (g <= 0.04045) g / 12.92 else Math.pow((g + 0.055) / 1.055, 2.4)
        val bLinear = if (b <= 0.04045) b / 12.92 else Math.pow((b + 0.055) / 1.055, 2.4)

        // Calculate relative luminance using the formula
        return 0.2126 * rLinear + 0.7152 * gLinear + 0.0722 * bLinear
    }

    /**
     * Get a safe alternative for red-green color combinations
     * @param originalColor The original color
     * @return A color that is safe for color blind users
     */
    @ColorInt
    fun getSafeAlternativeColor(@ColorInt originalColor: Int): Int {
        // If red or green, provide alternative
        val r = Color.red(originalColor)
        val g = Color.green(originalColor)
        val b = Color.blue(originalColor)

        // Check if it's primarily red
        return if (r > g * 1.5 && r > b * 1.5) {
            // Replace red with purple (more blue component)
            Color.argb(Color.alpha(originalColor), r, g, min(255, b + 100))
        }
        // Check if it's primarily green
        else if (g > r * 1.5 && g > b * 1.5) {
            // Replace green with teal (more blue component)
            Color.argb(Color.alpha(originalColor), r, g, min(255, b + 100))
        }
        // Not problematic, return original
        else {
            originalColor
        }
    }

    /**
     * Get a list of safe colors for charts and data visualization
     * @return List of color blind safe colors
     */
    fun getSafeColorPalette(): List<Int> {
        // IBM's color blind safe palette
        return listOf(
            Color.parseColor("#648FFF"),  // Blue
            Color.parseColor("#785EF0"),  // Purple
            Color.parseColor("#DC267F"),  // Magenta
            Color.parseColor("#FE6100"),  // Orange
            Color.parseColor("#FFB000")   // Yellow
        )
    }
}