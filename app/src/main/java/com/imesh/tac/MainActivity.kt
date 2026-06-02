package com.imesh.tac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.imesh.tac.magnet.MagnetData
import com.imesh.tac.magnet.MagnetShape
import com.imesh.tac.ui.theme.TACTheme

enum class Tab { HOME, ADD, PROFILE }

// SAMPLES
val sampleMagnets = listOf(
    MagnetData("rome",      "ROME",   "Colosseum",  Color(0xFFE8920F), Color(0xFFFFE0A0), MagnetShape.Arch,      40f,  80f, -4f, elevation = 3.dp),
    MagnetData("paris",     "PARIS",  "♥ 2024",     Color(0xFFC2185B), Color(0xFFFFD6EA), MagnetShape.Disc,     120f,  60f,  3f, elevation = 5.dp),
    MagnetData("nyc",       "NYC",    "New York",   Color(0xFF1A4FD6), Color(0xFFC8D8FF), MagnetShape.Shield,   202f,  70f,  2f, elevation = 4.dp),
    MagnetData("beach",     "Amalfi", "'23",        Color(0xFF0288D1), Color(0xFFE1F5FE), MagnetShape.Postcard,  32f, 190f, -2f, elevation = 3.dp),
    MagnetData("brasil",    "BRASIL", "",           Color(0xFF1A9C3E), Color(0xFFC8F5D8), MagnetShape.FlagStrip,145f, 168f,  1.5f, elevation = 2.dp),
    MagnetData("lisbon",    "LOVE",   "LISBON",     Color(0xFFE53935), Color(0xFFFFD0CF), MagnetShape.Heart,    252f, 185f, -3f, elevation = 6.dp),
    MagnetData("athens",    "ATHENS", "Parthenon",  Color(0xFF5040C8), Color(0xFFD8D0FF), MagnetShape.Arch,      60f, 300f,  3f, elevation = 4.dp),
    MagnetData("tokyo",     "TOKYO",  "JP",         Color(0xFF0277BD), Color(0xFFE1F5FE), MagnetShape.Disc,     170f, 280f, -5f, elevation = 7.dp),
    MagnetData("spain",     "ESPAÑA", "",           Color(0xFFB71C1C), Color(0xFFFFCDD2), MagnetShape.FlagStrip, 40f, 400f, -1f, elevation = 3.dp),
    MagnetData("santorini", "Santorini","Summer '24",Color(0xFFFF8F00), Color(0xFFFFF8E1), MagnetShape.Postcard, 148f, 380f,  2.5f, elevation = 5.dp),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TACTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(Tab.HOME) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        bottomBar = {
            NavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Fridge(sampleMagnets, modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun NavBar(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .height(64.dp)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.28f),
                        Color.White.copy(alpha = 0.12f)
                    )
                ),
                shape = RoundedCornerShape(32.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.7f),
                        Color.White.copy(alpha = 0.15f)
                    )
                ),
                shape = RoundedCornerShape(32.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        var addPressed by remember { mutableStateOf(false) }
        val addRotation by animateFloatAsState(
            targetValue = if (addPressed) 360f else 0f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            label = "addRotation"
        )
        val addScale by animateFloatAsState(
            targetValue = if (addPressed) 1.25f else 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "addScale"
        )
        val addIcon: ImageVector = if (addPressed) Icons.Default.Flight else Icons.Default.Add

        Box(
            Modifier
                .fillMaxWidth(0.6f)
                .height(1.5.dp)
                .align(Alignment.TopCenter)
                .offset(y = 6.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.6f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(1.dp)
                )
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = selectedTab == Tab.HOME,
                onClick = { onTabSelected(Tab.HOME) }
            )

            IconButton(
                onClick = {
                    addPressed = !addPressed
                    onTabSelected(Tab.ADD)
                },
                modifier = Modifier
                    .size(48.dp)
                    .graphicsLayer{
                        scaleX = addScale
                        scaleY = addScale
                        rotationZ = addRotation
                    }
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                Color.White.copy(alpha = 0.35f),
                                Color.White.copy(alpha = 0.08f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = addIcon,
                    contentDescription = "Add",
                    tint = if (selectedTab == Tab.ADD) Color.White else Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(22.dp)
                )
            }

            NavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                selected = selectedTab == Tab.PROFILE,
                onClick = { onTabSelected(Tab.PROFILE) }
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.15f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "navItemScale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (selected) 1f else 0.55f,
        animationSpec = tween(200),
        label = "navItemAlpha"
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = alpha),
            modifier = Modifier.size(24.dp)
        )
    }
}