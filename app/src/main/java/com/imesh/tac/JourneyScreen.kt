package com.imesh.tac

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imesh.tac.magnet.MagnetData
import com.imesh.tac.ui.theme.Background
import com.imesh.tac.ui.theme.inset
import com.imesh.tac.ui.theme.surface
import java.io.File

enum class JourneySection {
    MEDIA_HEADER,
    MAP_ROUTE,
    DESCRIPTION,
    GALLERY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyScreen(
    magnet: MagnetData,
    onBack: () -> Unit
) {
    var sectionOrder by remember {
        mutableStateOf(
            listOf(
                JourneySection.MEDIA_HEADER,
                JourneySection.MAP_ROUTE,
                JourneySection.DESCRIPTION,
                JourneySection.GALLERY
            )
        )
    }

    var isEditingMode by remember { mutableStateOf(false) }

    fun moveSectionUp(index: Int) {
        if (index > 0) {
            val mutable = sectionOrder.toMutableList()
            val target = mutable.removeAt(index)
            mutable.add(index - 1, target)
            sectionOrder = mutable
        }
    }

    fun moveSectionDown(index: Int) {
        if (index < sectionOrder.size - 1) {
            val mutable = sectionOrder.toMutableList()
            val target = mutable.removeAt(index)
            mutable.add(index + 1, target)
            sectionOrder = mutable
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${magnet.label} Journey",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                )
            )
        },
        containerColor = Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(sectionOrder) { section ->
                val current = sectionOrder.indexOf(section)

                SectionWrapper(
                    title = when(section) {
                        JourneySection.MEDIA_HEADER -> "Cover View"
                        JourneySection.MAP_ROUTE -> "Activity Route"
                        JourneySection.DESCRIPTION -> "Travel Logs & Vlogs"
                        JourneySection.GALLERY -> "Media"
                    },
                    isEditing = isEditingMode,
                    onToggleEdit = { isEditingMode = !isEditingMode },
                    onUp = if (current > 0) { { moveSectionUp(current) } } else null,
                    onDown = if (current < sectionOrder.size - 1) { { moveSectionDown(current) } } else null,
                    modifier = Modifier.animateContentSize()
                ) {
                    when(section) {
                        JourneySection.MEDIA_HEADER -> Media(magnet)
                        JourneySection.MAP_ROUTE -> Map(magnet)
                        JourneySection.DESCRIPTION -> Description(magnet)
                        JourneySection.GALLERY -> MediaGallery(magnet)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionWrapper(
    title: String,
    isEditing: Boolean,
    onToggleEdit: () -> Unit,
    onUp: (() -> Unit)?,
    onDown: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .surface()
            .pointerInput(isEditing) {
                detectTapGestures(
                    onLongPress = { onToggleEdit() },
                    onTap = { if (isEditing) onToggleEdit() }
                )
            }
    ) {
        AnimatedVisibility(
            visible = isEditing,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF7F8C8D)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (onUp != null) {
                        IconButton(
                            onClick = onUp,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.ArrowUpward, null, modifier = Modifier.size(14.dp), tint = Color(0xFF34495E))
                        }
                    }
                    if (onDown != null) {
                        IconButton(
                            onClick = onDown,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.ArrowDownward, null, modifier = Modifier.size(14.dp), tint = Color(0xFF34495E))
                        }
                    }
                }
            }
        }
        content()
    }
}

@Composable
fun Media(magnet: MagnetData) {
    val bitmap = remember(magnet.imagePath) {
        magnet.imagePath?.let { path ->
            val file = File(path)
            if (file.exists()) BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap() else null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(magnet.color)
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(magnet.color, magnet.accentColor)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(magnet.label, fontSize = 42.sp, fontWeight = FontWeight.Black, color = Color.White.copy(alpha = 0.25f))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = magnet.accentColor, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = magnet.label,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (magnet.subLabel.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(Icons.Default.DateRange, null, tint = Color.LightGray, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = magnet.subLabel,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Map(magnet: MagnetData) {}

@Composable
fun Description(magnet: MagnetData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .inset()
    ) {
        Text(
            text = "Bello ${magnet.label}",
            fontSize = 14.sp,
            color = Color(0xFF2C3E50),
            lineHeight = 22.sp
        )
    }
}

@Composable
fun MediaGallery(magnet: MagnetData) {
    // NO MEDIA YET
    val si = listOf(magnet.color, magnet.accentColor, magnet.color.copy(alpha = 0.6f), magnet.accentColor.copy(alpha = 0.5f))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 0..3) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(si[i % si.size].copy(alpha = 0.85f))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .shadow(2.dp)
            )
        }
    }
}