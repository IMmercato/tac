package com.imesh.tac

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.imesh.tac.magnet.MagnetData
import com.imesh.tac.ui.theme.TACTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TACTheme {
                Fridge(listOf())
            }
        }
    }
}

val MetalLight     = Color(0xFFF0F4F8)
val MetalMid       = Color(0xFFD1D9E6)
val MetalDark      = Color(0xFFB8C2D1)
val HandleColor    = Color(0xFF9AAABF)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Fridge(magnets: List<MagnetData>) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colorStops = arrayOf(0.00f to MetalLight, 0.35f to MetalMid, 0.70f to MetalDark, 1.00f to Color(0xFFA8B4C8)),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawBrushedLines()
        }
        Box(
            modifier = Modifier
                .height(140.dp)
                .width(7.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-14).dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF7A8FA8),
                            Color(0xFFCCD8E8),
                            Color(0xFF7A8FA8)
                        )
                    ),
                    RoundedCornerShape(3.dp)
                )
        )

        val sorted = remember(magnets) { magnets.sortedBy { it.elevation.value } }
        sorted.forEach { data ->
            key(data.id) { Magnet(data) }
        }
    }
}

private fun DrawScope.drawBrushedLines() {
    val spacing  = 48.dp.toPx()
    var x = 0f
    while (x < size.width) {
        drawLine(
            color = Color.White.copy(alpha = 0.06f),
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 1f
        )
        x += spacing
    }
}

@Composable
fun Magnet(data: MagnetData) {
    var offsetX by remember { mutableStateOf(data.initialX) }
    var offsetY by remember { mutableStateOf(data.initialY) }
    var isDragging by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
            .shadow(
                elevation = if (isDragging) 15.dp else 4.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color.Black.copy(alpha = 0.5f)
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false }
                ) { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .clip(RoundedCornerShape(8.dp))
            .drawBehind {
                drawRect(
                    color = Color.White.copy(alpha = 0.2f),
                    size = size.copy(height = size.height * 0.1f)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = data.label,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}