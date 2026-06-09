package com.imesh.tac.magnet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val subLabel: String,
    val color: Color,
    val accentColor: Color,
    val shape: MagnetShape,
    val initialX: Float,
    val initialY: Float,
    val initialRotation: Float = 0f,
    val elevation: Dp = 4.dp,
    val imagePath: String? = null
)


@Composable
fun ArchMagnet(data: MagnetData, elevation: Dp) {
    Box(
        modifier = Modifier
            .size(width = 54.dp, height = 70.dp)
            .shadow(elevation, RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        data.color.lighten(0.2f),
                        data.color,
                        data.color.darken(0.2f)
                    )
                ),
                RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
            )
            .drawBehind {
                drawRoundRect(
                    color  = Color.Black.copy(alpha = 0.28f),
                    topLeft = Offset(size.width * 0.25f, size.height * 0.28f),
                    size   = androidx.compose.ui.geometry.Size(size.width * 0.5f, size.height * 0.45f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.width * 0.25f, size.width * 0.25f)
                )
            },
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp))
                .background(Color.White.copy(alpha = 0.25f))
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Text(data.label,    color = data.accentColor, style = MaterialTheme.typography.labelSmall)
            Text(data.subLabel, color = data.accentColor.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp))
        }
    }
}

@Composable
fun DiscMagnet(data: MagnetData, elevation: Dp) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .shadow(elevation, CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(data.color.lighten(0.25f), data.color, data.color.darken(0.3f)),
                    center = Offset(0.35f, 0.35f)
                ),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color.Transparent)
                .drawBehind {
                    drawCircle(color = Color.White.copy(alpha = 0.15f), style = androidx.compose.ui.graphics.drawscope.Stroke(1.5f))
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(data.label,    color = data.accentColor, style = MaterialTheme.typography.labelSmall)
                Text(data.subLabel, color = data.accentColor.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall.copy(fontSize = 6.5.sp))
            }
        }
    }
}

@Composable
fun ShieldMagnet(data: MagnetData, elevation: Dp) {
    Box(
        modifier = Modifier
            .size(width = 46.dp, height = 58.dp)
            .shadow(elevation, GenericShape { size, _ ->
                moveTo(0f, 0f); lineTo(size.width, 0f)
                lineTo(size.width, size.height * 0.68f)
                lineTo(size.width / 2f, size.height)
                lineTo(0f, size.height * 0.68f)
                close()
            })
            .clip(GenericShape { size, _ ->
                moveTo(0f, 0f); lineTo(size.width, 0f)
                lineTo(size.width, size.height * 0.68f)
                lineTo(size.width / 2f, size.height)
                lineTo(0f, size.height * 0.68f)
                close()
            })
            .background(Brush.verticalGradient(listOf(data.color.lighten(0.15f), data.color, data.color.darken(0.25f)))),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text(data.label,    color = data.accentColor, style = MaterialTheme.typography.labelSmall)
            Text(data.subLabel, color = data.accentColor.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp))
        }
    }
}

@Composable
fun PostcardMagnet(data: MagnetData, elevation: Dp) {
    Column(
        modifier = Modifier
            .size(width = 80.dp, height = 54.dp)
            .shadow(elevation, RoundedCornerShape(3.dp))
            .background(Color(0xFFF4EED6), RoundedCornerShape(3.dp))
            .drawBehind {
                drawRoundRect(
                    color  = Color(0xFFB8A06A).copy(alpha = 0.45f),
                    style  = androidx.compose.ui.graphics.drawscope.Stroke(1.5f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f)
                )
            }
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Brush.linearGradient(listOf(data.color.lighten(0.1f), data.color.darken(0.1f))))
        )
        Column(Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) {
            Text(data.label,    color = Color(0xFF3C2A0A), style = MaterialTheme.typography.labelSmall)
            Text(data.subLabel, color = Color(0xFF7A6030), style = MaterialTheme.typography.labelSmall.copy(fontSize = 6.5.sp))
        }
    }
}

@Composable
fun FlagStripMagnet(data: MagnetData, elevation: Dp) {
    Row(
        modifier = Modifier
            .size(width = 72.dp, height = 36.dp)
            .shadow(elevation, RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 14.dp, bottomEnd = 14.dp))
            .background(
                Brush.horizontalGradient(listOf(data.color, data.color.darken(0.15f))),
                RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 14.dp, bottomEnd = 14.dp)
            )
            .drawBehind {
                drawRoundRect(
                    color = data.accentColor.copy(alpha = 0.9f),
                    size  = androidx.compose.ui.geometry.Size(10.dp.toPx(), size.height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f)
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(16.dp))
        Text(data.label, color = data.accentColor, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun HeartMagnet(data: MagnetData, elevation: Dp) {
    val heartShape = GenericShape { size, _ ->
        val w = size.width; val h = size.height
        moveTo(w / 2, h * 0.85f)
        cubicTo(w * 0.05f, h * 0.6f,  w * 0.05f, h * 0.35f, w * 0.25f, h * 0.2f)
        cubicTo(w * 0.38f, h * 0.08f, w / 2, h * 0.15f, w / 2, h * 0.25f)
        cubicTo(w / 2, h * 0.15f, w * 0.62f, h * 0.08f, w * 0.75f, h * 0.2f)
        cubicTo(w * 0.95f, h * 0.35f, w * 0.95f, h * 0.6f,  w / 2, h * 0.85f)
        close()
    }
    Box(
        modifier = Modifier
            .size(52.dp, 50.dp)
            .shadow(elevation, heartShape)
            .clip(heartShape)
            .background(Brush.linearGradient(listOf(data.color.lighten(0.2f), data.color, data.color.darken(0.3f)))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Text(data.label,    color = data.accentColor, style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.5.sp))
            Text(data.subLabel, color = data.accentColor.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall.copy(fontSize = 6.5.sp))
        }
    }
}

fun Color.lighten(by: Float): Color = copy(
    red   = (red   + by).coerceIn(0f, 1f),
    green = (green + by).coerceIn(0f, 1f),
    blue  = (blue  + by).coerceIn(0f, 1f)
)

fun Color.darken(by: Float): Color = copy(
    red   = (red   - by).coerceIn(0f, 1f),
    green = (green - by).coerceIn(0f, 1f),
    blue  = (blue  - by).coerceIn(0f, 1f)
)