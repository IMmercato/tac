package com.imesh.tac.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class TACColors(
    val primary: Color,
    val background: Color,
    val onBackground: Color
)

private val LightColors = TACColors(
    primary = LightPrimary,
    background = LightBackground,
    onBackground = LightOnBackground
)

private val DarkColors = TACColors(
    primary = DarkPrimary,
    background = DarkBackground,
    onBackground = DarkOnBackground
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    background = LightBackground,
    onBackground = LightOnBackground
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    background = DarkBackground,
    onBackground = DarkOnBackground
)

@Composable
fun TACTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}

// Neu Modifiers
fun Modifier.surface(radius: Float = 16f) = this
    .drawBehind {
        drawRoundRect(
            color = Light,
            topLeft = Offset(-4.dp.toPx(), 4.dp.toPx()),
            size = size,
            cornerRadius = CornerRadius(radius.dp.toPx(), radius.dp.toPx())
        )
        drawRoundRect(
            color = Shadow,
            topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
            size = size,
            cornerRadius = CornerRadius(radius.dp.toPx(), radius.dp.toPx())
        )
    }
    .background(Background, RoundedCornerShape(radius.dp))

fun Modifier.inset(radius: Float = 12f) = this
    .drawBehind {
        drawRoundRect(
            color = Shadow,
            topLeft = Offset(0f, 0f),
            size = size,
            cornerRadius = CornerRadius(radius.dp.toPx(), radius.dp.toPx())
        )
        drawRoundRect(
            color = Light,
            topLeft = Offset(2.dp.toPx(), 2.dp.toPx()),
            size = size,
            cornerRadius = CornerRadius(radius.dp.toPx(), radius.dp.toPx())
        )
    }
    .background(Background.copy(alpha = 0.4f), RoundedCornerShape(radius.dp))