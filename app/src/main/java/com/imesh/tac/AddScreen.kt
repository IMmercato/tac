package com.imesh.tac

import android.util.DisplayMetrics
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imesh.tac.magnet.MagnetData
import com.imesh.tac.magnet.MagnetShape
import com.imesh.tac.magnet.lighten
import kotlin.random.Random

private val PanelDark   = Color(0xFF1A202C)
private val PanelMid    = Color(0xFF2D3748)
private val PanelLight  = Color(0xFF4A5568)
private val AccentGold  = Color(0xFFE8C882)
private val TextPrimary = Color(0xFFE8EDF2)
private val TextMuted   = Color(0xFF6B7A8D)
private val InputBg     = Color(0xFF252F3D)
private val InputBorder = Color(0xFF3A4A5C)

private data class ColorPalette(val name: String, val main: Color, val accent: Color)

private val colorPalettes = listOf(
    ColorPalette("Amber",   Color(0xFFE8920F), Color(0xFFFFE0A0)),
    ColorPalette("Rose",    Color(0xFFC2185B), Color(0xFFFFD6EA)),
    ColorPalette("Cobalt",  Color(0xFF1A4FD6), Color(0xFFC8D8FF)),
    ColorPalette("Sky",     Color(0xFF0288D1), Color(0xFFE1F5FE)),
    ColorPalette("Forest",  Color(0xFF1A9C3E), Color(0xFFC8F5D8)),
    ColorPalette("Crimson", Color(0xFFE53935), Color(0xFFFFD0CF)),
    ColorPalette("Violet",  Color(0xFF5040C8), Color(0xFFD8D0FF)),
    ColorPalette("Slate",   Color(0xFF455A64), Color(0xFFCFD8DC)),
)

private data class Shapes(val shape: MagnetShape, val label: String)

private val shapeOptions = listOf(
    Shapes(MagnetShape.Arch,      "Arch"),
    Shapes(MagnetShape.Disc,      "Disc"),
    Shapes(MagnetShape.Shield,    "Shield"),
    Shapes(MagnetShape.Postcard,  "Postcard"),
    Shapes(MagnetShape.Heart,     "Heart"),
    Shapes(MagnetShape.FlagStrip, "Flag"),
)

private enum class Steps { DETAILS, SHAPE, CONFIRM }

@Composable
fun AddScreen(onMagnetCreated: (MagnetData) -> Unit = {}) {
    var step by remember { mutableStateOf(Steps.DETAILS) }
    var destination by remember { mutableStateOf("") }
    var subLabel by remember { mutableStateOf("") }
    var selectedShape by remember { mutableStateOf<MagnetShape>(MagnetShape.Arch) }
    var selectedColor by remember { mutableIntStateOf(0) }
    var useCustom by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colorStops = arrayOf(
                        0.00f to Color(0xFFF0F4F8),
                        0.35f to Color(0xFFD1D9E6),
                        0.70f to Color(0xFFB8C2D1),
                        1.00f to Color(0xFFA8B4C8)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Title(step)
        StepIndicator(step)

        AnimatedContent(
            targetState = step,
            transitionSpec = {
                (fadeIn(tween(280)) + slideInVertically(tween(280)) { it / 6 })
                    .togetherWith(fadeOut(tween(180)) + slideOutVertically(tween(180)) { -it / 6 })
            },
            label = "stepContent"
        ) { currentStep ->
            when (currentStep) {
                Steps.DETAILS -> Details(
                    destination = destination,
                    subLabel = subLabel,
                    onDestinationChange = { destination = it },
                    onSubLabelChange = { subLabel = it },
                    onNext = { if (destination.isNotBlank()) step = Steps.SHAPE }
                )
                Steps.SHAPE -> Shape(
                    selectedShape = selectedShape,
                    selectedColor = selectedColor,
                    destination = destination,
                    subLabel = subLabel,
                    useCustom = useCustom,
                    onShapeSelect = { selectedShape = it },
                    onColorSelect = { selectedColor = it },
                    onToggleCustom = { useCustom = it },
                    onBack = { step = Steps.DETAILS },
                    onNext = { step = Steps.CONFIRM }
                )
                Steps.CONFIRM -> Confirm(
                    destination = destination,
                    subLabel = subLabel,
                    shape = selectedShape,
                    palette = colorPalettes[selectedColor],
                    onBack = { step = Steps.SHAPE },
                    onConfirm = {
                        onMagnetCreated(
                            MagnetData(
                                id = "magnet_${System.currentTimeMillis()}",
                                label = destination.uppercase().take(10),
                                subLabel = subLabel,
                                color = colorPalettes[selectedColor].main,
                                accentColor = colorPalettes[selectedColor].accent,
                                shape = selectedShape,
                                initialX = Random.nextFloat() * DisplayMetrics().widthPixels,
                                initialY = Random.nextFloat() * DisplayMetrics().heightPixels,
                                initialRotation = (-4..4).random().toFloat()
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun Title(step: Steps) {
    val title = when (step) {
        Steps.DETAILS -> "New Magnet"
        Steps.SHAPE   -> "Pick a Style"
        Steps.CONFIRM -> "All Set?"
    }
    val subtitle = when (step) {
        Steps.DETAILS -> "Where did you travel?"
        Steps.SHAPE   -> "Choose shape & colour"
        Steps.CONFIRM -> "Preview your magnet"
    }

    Column {
        Text(
            text = title,
            color = PanelDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = subtitle,
            color = PanelLight,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
private fun StepIndicator(current: Steps) {
    val steps = Steps.entries
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        steps.forEach { steps ->
            val active = steps == current
            val passed = steps.ordinal < current.ordinal
            val width by animateDpAsState(if (active) 24.dp else 8.dp, tween(300), label = "dot")

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(
                        when {
                            active -> AccentGold
                            passed -> PanelMid
                            else -> Color(0xFFCDD6E0)
                        }
                    )
            )
        }
    }
}

@Composable
private fun Details(
    destination: String,
    subLabel: String,
    onDestinationChange: (String) -> Unit,
    onSubLabelChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MetalCard {
            Column(
                Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FieldLabel("DESTINATION")
                TextField(
                    value = destination,
                    onValueChange = onDestinationChange,
                    placeholder = "e.g. Rome, Colombo, LA...",
                    leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = AccentGold, modifier = Modifier.size(18.dp)) },
                    imeAction = ImeAction.Next
                )
                FieldLabel("MEMORY TAG (optional)")
                TextField(
                    value = subLabel,
                    onValueChange = onSubLabelChange,
                    placeholder = "e.g. Summer '24, ♥ 2023…",
                    imeAction = ImeAction.Done,
                    onDone = onNext
                )
            }
        }

        Button(
            label = "Choose Style →",
            enabled = destination.isNotBlank(),
            onClick = onNext
        )
    }
}

@Composable
private fun Shape(
    selectedShape: MagnetShape,
    selectedColor: Int,
    destination: String,
    subLabel: String,
    useCustom: Boolean,
    onShapeSelect: (MagnetShape) -> Unit,
    onColorSelect: (Int) -> Unit,
    onToggleCustom: (Boolean) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val palette = colorPalettes[selectedColor]

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MetalCard {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FieldLabel("PREVIEW")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                colorStops = arrayOf(
                                    0f to Color(0xFFD1D9E6),
                                    1f to Color(0xFFA8B4C8)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    MagnetBody(
                        data = MagnetData(
                            id = "preview",
                            label = destination.uppercase().take(10),
                            subLabel = subLabel,
                            color = palette.main,
                            accentColor = palette.accent,
                            shape = selectedShape,
                            initialX = 0f,
                            initialY = 0f
                        ),
                        elevation = 8.dp
                    )
                }
            }
        }

        MetalCard {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FieldLabel("SHAPE")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    itemsIndexed(shapeOptions) { _, option ->
                        val active = option.shape == selectedShape
                        ShapeChip(
                            label = option.label,
                            selected = active,
                            onClick = { onShapeSelect(option.shape) }
                        )
                    }
                }
            }
        }

        MetalCard {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FieldLabel("COLOR")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    itemsIndexed(colorPalettes) { index, palette ->
                        ColorSwatch(
                            palette = palette,
                            selected = index == selectedColor,
                            onClick = { onColorSelect(index) }
                        )
                    }
                }
            }
        }

        MetalCard {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                FieldLabel("OR USE YOUR OWN PHOTO")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MediaButton(
                        icon = Icons.Default.CameraAlt,
                        label = "Camera",
                        selected = useCustom,
                        onClick = { onToggleCustom(true) }
                    )
                    MediaButton(
                        icon = Icons.Default.PhotoLibrary,
                        label = "Gallery",
                        selected = useCustom,
                        onClick = { onToggleCustom(true) }
                    )
                }
                AnimatedVisibility(visible = useCustom) {
                    Text(
                        "Custom image selected - will be placed on a Postcard shape.",
                        color = AccentGold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button2(label = "← Back", onClick = onBack, modifier = Modifier.weight(1f))
            Button(label = "Preview →", onClick = onNext, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun Confirm(
    destination: String,
    subLabel: String,
    shape: MagnetShape,
    palette: ColorPalette,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MetalCard {
            Column(
                Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFFCDD6E0), Color(0xFF9AAABF))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    MagnetBody(
                        data = MagnetData(
                            id = "confirm_preview",
                            label = destination.uppercase().take(10),
                            subLabel = subLabel,
                            color = palette.main,
                            accentColor = palette.accent,
                            shape = shape,
                            initialX = 0f,
                            initialY = 0f
                        ),
                        elevation = 16.dp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = destination.uppercase(),
                        color = PanelDark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )
                    if (subLabel.isNotBlank()) {
                        Text(
                            text = subLabel,
                            color = PanelLight,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    SummaryTag(
                        label = shapeOptions.first { it.shape == shape }.label,
                        color = palette.main,
                        accent = palette.accent
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button2(label = "← Edit", onClick = onBack, modifier = Modifier.weight(1f))
            ConfirmButton(onClick = onConfirm, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun MetalCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.00f to Color(0xFF5A6A7D),
                        0.10f to Color(0xFF3A4A5C),
                        0.50f to Color(0xFF2D3748),
                        1.00f to Color(0xFF1A202C)
                    )
                ),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color(0xFFCDD6E0).copy(alpha = 0.5f), Color(0xFF1A202C))
                ),
                RoundedCornerShape(16.dp)
            )
    ) {
        content()
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = TextMuted,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    )
}

@Composable
private fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: (@Composable () -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Default,
    onDone: (() -> Unit)? = null
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
        cursorBrush = SolidColor(AccentGold),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onDone = { onDone?.invoke() }),
        decorationBox = { inner ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(InputBg, RoundedCornerShape(10.dp))
                    .border(1.dp, InputBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(placeholder, color = TextMuted, fontSize = 15.sp)
                    }
                    inner()
                }
            }
        }
    )
}

@Composable
private fun ShapeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        if (selected) 1.06f else 1f,
        spring(Spring.DampingRatioMediumBouncy),
        label = "chip"
    )
    Box(
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (selected)
                    Brush.linearGradient(listOf(AccentGold.lighten(0.1f), AccentGold))
                else
                    Brush.linearGradient(listOf(InputBg, InputBg))
            )
            .border(
                1.dp,
                if (selected) AccentGold else InputBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) PanelDark else TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ColorSwatch(palette: ColorPalette, selected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        if (selected) 1.15f else 1f,
        spring(Spring.DampingRatioMediumBouncy),
        label = "swatch"
    )
    Box(
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .size(36.dp)
            .shadow(if (selected) 6.dp else 2.dp, CircleShape)
            .clip(CircleShape)
            .background(palette.main)
            .border(
                width = if (selected) 2.5.dp else 0.dp,
                color = Color.White,
                shape = CircleShape
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(Icons.Default.Check, null, tint = palette.accent, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun SummaryTag(label: String, color: Color, accent: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color)
            .padding(horizontal = 14.dp, vertical = 5.dp)
    ) {
        Text(label, color = accent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
    }
}

@Composable
private fun Button(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .shadow(if (enabled) 8.dp else 0.dp, RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .background(
                if (enabled)
                    Brush.linearGradient(
                        listOf(
                            AccentGold.lighten(0.1f),
                            AccentGold,
                            AccentGold.copy(red = AccentGold.red * 0.9f)
                        )
                    )
                else
                    Brush.linearGradient(listOf(PanelLight, PanelMid))
            )
            .alpha(if (enabled) 1f else 0.5f)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (enabled) PanelDark else TextMuted,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
private fun Button2(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(InputBg)
            .border(1.dp, InputBorder, RoundedCornerShape(25.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MediaButton(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) PanelMid else InputBg)
            .border(1.dp, if (selected) AccentGold.copy(alpha = 0.5f) else InputBorder, RoundedCornerShape(10.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, null, tint = if (selected) AccentGold else TextMuted, modifier = Modifier.size(16.dp))
        Text(label, color = if (selected) AccentGold else TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ConfirmButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(50.dp)
            .shadow(10.dp, RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .background(
                Brush.linearGradient(listOf(Color(0xFF2ECC71), Color(0xFF27AE60)))
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
            Text("Add to Fridge", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black)
        }
    }
}