package com.imesh.tac.magnet

import android.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class MagnetShape {
    object Arch : MagnetShape()
    object Disc : MagnetShape()
    object Shield : MagnetShape()
    object Postcard : MagnetShape()
    object FlagStrip : MagnetShape()
    object Heart : MagnetShape()
}

data class MagnetData(
    val id: String,
    val label: String,
    val sublabel: String = "",
    val color: Color,
    val accentColor: Color,
    val shape: MagnetShape,
    val initialX: Float,
    val initialY: Float,
    val initialRotation: Float = 0f,
    val elevation: Dp = 4.dp
)

