package com.imesh.tac

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.imesh.tac.magnet.ArchMagnet
import com.imesh.tac.magnet.DiscMagnet
import com.imesh.tac.magnet.FlagStripMagnet
import com.imesh.tac.magnet.HeartMagnet
import com.imesh.tac.magnet.MagnetData
import com.imesh.tac.magnet.MagnetShape
import com.imesh.tac.magnet.PostcardMagnet
import com.imesh.tac.magnet.ShieldMagnet
import com.imesh.tac.ui.theme.MetalDark
import com.imesh.tac.ui.theme.MetalLight
import com.imesh.tac.ui.theme.MetalMid

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Fridge(
    magnets: List<MagnetData>,
    onMagnetMoved: (id: String, x: Float, y: Float) -> Unit,
    onMagnetDelete: (id: String) -> Unit,
    onMagnetTapped: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier
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
                    Brush.horizontalGradient(listOf(Color(0xFF7A8FA8), Color(0xFFCCD8E8), Color(0xFF7A8FA8))),
                    RoundedCornerShape(3.dp)
                )
        )

        val sorted = remember(magnets) { magnets.sortedBy { it.elevation.value } }
        sorted.forEach { data ->
            key(data.id) {
                Magnet(
                    data = data,
                    onDragEnd = { x, y -> onMagnetMoved(data.id, x, y) },
                    onDelete = { onMagnetDelete(data.id) },
                    onTap = { onMagnetTapped(data.id) }
                )
            }
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
fun Magnet(data: MagnetData, onDragEnd: (Float, Float) -> Unit, onDelete: () -> Unit, onTap: () -> Unit) {
    var offsetX by remember { mutableFloatStateOf(data.initialX) }
    var offsetY by remember { mutableFloatStateOf(data.initialY) }
    var isDragging by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    val liftedElevation = data.elevation + 12.dp

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
            .pointerInput(data.id) {
                detectTapGestures(
                    onLongPress = { showMenu = true },
                    onTap = { onTap() }
                )
            }
            .pointerInput(data.id) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        onDragEnd(offsetX, offsetY)
                    },
                    onDragCancel = {
                        isDragging = false
                        onDragEnd(offsetX, offsetY)
                    }
                ) { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .graphicsLayer {
                rotationZ = if (isDragging) data.initialRotation * 0.5f else data.initialRotation
                scaleX = if (isDragging) 1.06f else 1f
                scaleY = if (isDragging) 1.06f else 1f
            }
    ) {
        MagnetBody(
            data = data,
            elevation = if (isDragging) liftedElevation else data.elevation,
            overrideBitmap = null
        )

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Icon(Icons.Default.Delete, "delete") },
                onClick = {
                    showMenu = false
                    onDelete()
                }
            )
            DropdownMenuItem(
                text = { Icon(Icons.Default.ModeEdit, "edit") },
                onClick = {
                    showMenu = false
                }
            )
        }
    }
}

@Composable
fun MagnetBody(
    data: MagnetData,
    elevation: Dp,
    overrideBitmap: ImageBitmap?
) {
    when (data.shape) {
        MagnetShape.Arch -> ArchMagnet(data, elevation)
        MagnetShape.Disc -> DiscMagnet(data, elevation)
        MagnetShape.Shield -> ShieldMagnet(data, elevation)
        MagnetShape.Postcard -> PostcardMagnet(data, elevation)
        MagnetShape.Heart -> HeartMagnet(data, elevation)
        MagnetShape.FlagStrip -> FlagStripMagnet(data, elevation)
    }
}