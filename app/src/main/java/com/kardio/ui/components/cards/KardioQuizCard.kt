package com.kardio.ui.components.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.TextSecondary

/**
 * Quiz card component for Kardio.
 * Displays a quiz question with an optional index indicator.
 *
 * @param question The question text to display
 * @param indexText Optional index indicator (e.g., "1/10")
 * @param questionTextSize Text size for the question
 * @param onClick Optional callback for when the card is clicked
 * @param modifier Modifier to be applied to the card
 */
@Composable
fun KardioQuizCard(
    question: String,
    indexText: String? = null,
    questionTextSize: TextUnit = 20.sp,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    KardioCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Question text
                Text(
                    text = question,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = questionTextSize
                    ),
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Index indicator at the bottom right
                if (indexText != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Text(
                            text = indexText,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioQuizCardPreview() {
    KardioTheme {
        KardioQuizCard(
            question = "What is the capital of France?",
            indexText = "1/10",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioQuizCardLongTextPreview() {
    KardioTheme {
        KardioQuizCard(
            question = "If a train travels at 120 km/h and another train travels at 80 km/h in opposite directions, how far apart will they be after 3 hours?",
            indexText = "5/10",
            questionTextSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}