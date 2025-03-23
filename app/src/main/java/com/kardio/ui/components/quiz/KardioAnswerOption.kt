package com.kardio.ui.components.quiz

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.Accent
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.Divider
import com.kardio.ui.theme.Error
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.Success
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.TextSecondary

/**
 * Answer option states
 */
enum class AnswerOptionState {
    NORMAL,
    SELECTED,
    CORRECT,
    INCORRECT
}

/**
 * Answer option component for Kardio quizzes.
 * Displays an option in a quiz with an indicator (e.g., A, B, C, D) and text.
 *
 * @param text The answer option text
 * @param indicator The indicator text (e.g., "A", "B", "C", "D")
 * @param state The current state of the answer option
 * @param enabled Whether the option is enabled
 * @param onClick Callback for when the option is clicked
 * @param modifier Modifier to be applied to the option
 */
@Composable
fun KardioAnswerOption(
    text: String,
    indicator: String,
    state: AnswerOptionState = AnswerOptionState.NORMAL,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current

    // Animations
    val scale by animateFloatAsState(
        targetValue = if (state == AnswerOptionState.SELECTED && enabled) 0.98f else 1f,
        label = "ScaleAnimation"
    )

    // Colors based on state
    val borderColor by animateColorAsState(
        targetValue = when (state) {
            AnswerOptionState.NORMAL -> Divider
            AnswerOptionState.SELECTED -> Accent
            AnswerOptionState.CORRECT -> Success
            AnswerOptionState.INCORRECT -> Error
        },
        label = "BorderColorAnimation"
    )

    val indicatorColor by animateColorAsState(
        targetValue = when (state) {
            AnswerOptionState.NORMAL -> TextSecondary
            AnswerOptionState.SELECTED -> Accent
            AnswerOptionState.CORRECT -> Success
            AnswerOptionState.INCORRECT -> Error
        },
        label = "IndicatorColorAnimation"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                enabled = enabled && state == AnswerOptionState.NORMAL,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            ),
        shape = RoundedCornerShape(8.dp),
        color = BackgroundSecondary,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicator (A, B, C, D, etc.)
            Text(
                text = indicator,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = indicatorColor,
                modifier = Modifier.padding(end = 16.dp)
            )

            // Option text
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
        }
    }

    // Apply alpha for disabled state
    if (!enabled) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(8.dp)
                .clickable(enabled = false) { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioAnswerOptionNormalPreview() {
    KardioTheme {
        KardioAnswerOption(
            text = "Paris",
            indicator = "A",
            state = AnswerOptionState.NORMAL,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioAnswerOptionSelectedPreview() {
    KardioTheme {
        KardioAnswerOption(
            text = "London",
            indicator = "B",
            state = AnswerOptionState.SELECTED,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioAnswerOptionCorrectPreview() {
    KardioTheme {
        KardioAnswerOption(
            text = "Berlin",
            indicator = "C",
            state = AnswerOptionState.CORRECT,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioAnswerOptionIncorrectPreview() {
    KardioTheme {
        KardioAnswerOption(
            text = "Madrid",
            indicator = "D",
            state = AnswerOptionState.INCORRECT,
            modifier = Modifier.padding(16.dp)
        )
    }
}