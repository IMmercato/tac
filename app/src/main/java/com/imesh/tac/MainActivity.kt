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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.imesh.tac.magnet.MagnetDatabase
import com.imesh.tac.magnet.MagnetRepository
import com.imesh.tac.ui.theme.TACTheme
import kotlinx.coroutines.launch

enum class Tab { HOME, ADD, PROFILE }

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
    val context = LocalContext.current
    val repository = remember {
        val db = MagnetDatabase.getInstance(context)
        MagnetRepository(db.magnetDao())
    }
    val magnets by repository.getAllMagnets().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        repository.insertSampleIfEmpty(context)
    }

    var selectedTab by remember { mutableStateOf(Tab.HOME) }

    Box(modifier = Modifier.fillMaxSize()) {
        when (selectedTab) {
            Tab.HOME -> Fridge(
                magnets = magnets,
                onMagnetMoved = { id, x, y ->
                    scope.launch { repository.updatePosition(id, x, y) }
                },
                onMagnetDelete = { id ->
                    scope.launch { repository.deleteMagnet(id) }
                }
            )
            Tab.ADD -> AddScreen(
                onMagnetCreated = { newMagnet ->
                    scope.launch { repository.insertMagnet(newMagnet) }
                    selectedTab = Tab.HOME
                }
            )
            Tab.PROFILE -> ProfileScreen()
        }

        NavBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 32.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun NavBar(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
    modifier: Modifier = Modifier
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
    val addIcon: ImageVector = if (addPressed) Icons.Default.FlightTakeoff else Icons.Default.Add

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.00f to Color(0xFF6B7A8D),
                        0.08f to Color(0xFF4A5568),
                        0.50f to Color(0xFF2D3748),
                        0.92f to Color(0xFF1A202C),
                        1.00f to Color(0xFF0F141A)
                    )
                ),
                shape = RoundedCornerShape(31.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFFCDD6E0),
                        0.4f to Color(0xFF7A8FA8),
                        1.0f to Color(0xFF1A202C)
                    )
                ),
                shape = RoundedCornerShape(31.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.42f)
                .align(Alignment.TopCenter)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 31.dp, topEnd = 31.dp)
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
                    .size(44.dp)
                    .graphicsLayer{
                        scaleX = addScale
                        scaleY = addScale
                        rotationZ = addRotation
                    }
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f  to Color(0xFF8A9BB0),
                                0.3f  to Color(0xFF5A6A7D),
                                0.7f  to Color(0xFF3A4A5C),
                                1.0f  to Color(0xFF252F3D),
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(
                        1.dp,
                        brush = Brush.verticalGradient(
                            listOf(
                                Color(0xFFCDD6E0),
                                Color(0xFF3A4A5C)
                            )
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = addIcon,
                    contentDescription = "Add",
                    tint = if (addPressed) Color(0xFFFFE0A0) else Color(0xFFCDD6E0),
                    modifier = Modifier.size(20.dp)
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
        targetValue = if (selected) 1.18f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
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
            tint = if (selected) Color(0xFFE8EDF2) else Color(0xFF6B7A8D),
            modifier = Modifier.size(24.dp)
        )
    }
}