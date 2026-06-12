package com.imesh.tac

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imesh.tac.magnet.MagnetData

val Background = Color(0xFFEFEFEF)
val LightHighlight = Color(0xFFFFFFFF)
val DarkShadow = Color(0xFFD1D9E6)
val Orange = Color(0xFFFC6100)

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
                    onUp = if (current > 0) { { moveSectionUp(current) } } else null,
                    onDown = if (current < sectionOrder.size - 1) { { moveSectionDown(current) } } else null,
                    modifier = Modifier.animateContentSize()
                ) {
                    when(section) {
                        JourneySection.MEDIA_HEADER -> Media()
                        JourneySection.MAP_ROUTE -> Map()
                        JourneySection.DESCRIPTION -> Description()
                        JourneySection.GALLERY -> MediaGallery()
                    }
                }
            }
        }
    }
}

@Composable
fun SectionWrapper(
   title: String,
   onUp: (() -> Unit)?,
   onDown: (() -> Unit)?,
   modifier: Modifier = Modifier,
   content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
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
        content()
    }
}

@Composable
fun Media() {}

@Composable
fun Map() {}

@Composable
fun Description() {}

@Composable
fun MediaGallery() {}