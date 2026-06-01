package com.imesh.tac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        bottomBar = {
            BottomAppBar {
                IconButton(onClick = { selectedTab = Tab.HOME },
                    modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Home",
                        tint = if (selectedTab == Tab.HOME) Color.White else Color.DarkGray
                    )
                }
                IconButton(onClick = { selectedTab = Tab.ADD },
                    modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Add,
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Add",
                        tint = if (selectedTab == Tab.ADD) Color.White else Color.DarkGray
                    )
                }
                IconButton(onClick = { selectedTab = Tab.PROFILE },
                    modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = if (selectedTab == Tab.PROFILE) Color.White else Color.DarkGray
                    )
                }
            }
        }
    ) {paddingValues ->
        Fridge(sampleMagnets, modifier = Modifier.padding(paddingValues))
    }
}