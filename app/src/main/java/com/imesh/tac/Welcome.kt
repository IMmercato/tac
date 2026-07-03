package com.imesh.tac

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imesh.tac.ui.theme.AccentGold
import com.imesh.tac.ui.theme.Background
import com.imesh.tac.ui.theme.TextMuted

private data class OnboardPage(
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Brush,
    val eyebrow: String,
    val title: String,
    val body: String
)

private val pages = listOf(
    OnboardPage(
        icon = Icons.Default.FlightTakeoff,
        iconTint = AccentGold,
        iconBg = Brush.radialGradient(
            listOf(Color(0xFF3A2A00), Color(0xFF1A1200))
        ),
        eyebrow = "WELCOME TO TAC",
        title = "Your fridge, \nyour world.",
        body = "TAC turns every trip into a fridge magnet — a tiny artefact that lives on your virtual door, forever."
    ),
    OnboardPage(
        icon = Icons.Default.Explore,
        iconTint = Color(0xFF64B5F6),
        iconBg = Brush.radialGradient(
            listOf(Color(0xFF0A1A2A), Color(0xFF050D15))
        ),
        eyebrow = "BUILD YOUR COLLECTION",
        title = "Pick a shape,\npin a place.",
        body = "Choose from six magnet shapes — arch, disc, shield, postcard, heart, flag. Each one a destination, a date, a story."
    ),
    OnboardPage(
        icon = Icons.Default.PhotoLibrary,
        iconTint = Color(0xFFA78BFA),
        iconBg = Brush.radialGradient(
            listOf(Color(0xFF160A2A), Color(0xFF0B0515))
        ),
        eyebrow = "CAPTURE THE MOMENT",
        title = "Add a photo,\nrelive the memory.",
        body = "Attach a cover shot from your camera or gallery. Tap your magnet anytime to open the full journey view."
    ),
    OnboardPage(
        icon = Icons.Default.Share,
        iconTint = Color(0xFF4DD0A4),
        iconBg = Brush.radialGradient(
            listOf(Color(0xFF002A1A), Color(0xFF001510))
        ),
        eyebrow = "SHARE THE FRIDGE",
        title = "One code.\nEvery magnet.",
        body = "Generate a share code and let friends and family see — and add to — your fridge. Travel is better together."
    )
)

@Composable
fun Welcome(onFinished: () -> Unit = {}) {
    var pageIndex by remember { mutableIntStateOf(0) }
    var slideDirection by remember { mutableIntStateOf(1) }

    fun goNext() {
        if (pageIndex < pages.size - 1) {
            slideDirection = 1
            pageIndex++
        } else {
            onFinished()
        }
    }

    fun goPrev() {
        if (pageIndex > 0) {
            slideDirection = -1
            pageIndex--
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) { drawSubtleLinesWelcome() }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row() { }

            AnimatedContent(
                targetState = pageIndex,
                transitionSpec = {
                    val dir = slideDirection
                    (fadeIn(tween(320)) + slideInHorizontally(tween(320, easing = { t ->
                        val inv = 1f - t; 1f - inv * inv * inv
                    })) { (it * 0.25f * dir).toInt() })
                        .togetherWith(
                            fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { (-it * 0.15f * dir).toInt() }
                        )
                },
                label = "page",
                modifier = Modifier.weight(1f)
            ) { idx ->
                PageContent(page = pages[idx], modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun PageContent(page: OnboardPage, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .shadow(20.dp, CircleShape, ambientColor = Color.Black, spotColor = Color.Black.copy(alpha = 0.8f))
                .clip(CircleShape)
                .background(page.iconBg)
                .border(
                    1.dp,
                    Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.12f), Color.Transparent)
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                tint = page.iconTint,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(40.dp))

        Text(
            text = page.eyebrow,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            color = AccentGold.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = page.title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = AccentGold,
            textAlign = TextAlign.Center,
            lineHeight = 38.sp
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = page.body,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

private fun DrawScope.drawSubtleLinesWelcome() {
    val spacing = 56.dp.toPx()
    var x = 0f
    while (x < size.width) {
        drawLine(
            color = Color.White.copy(alpha = 0.03f),
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 1f
        )
        x += spacing
    }
}