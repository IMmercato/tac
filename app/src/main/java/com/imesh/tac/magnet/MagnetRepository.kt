package com.imesh.tac.magnet

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class MagnetRepository(private val dao: MagnetDao) {
    fun getAllMagnets(): Flow<List<MagnetData>> = dao.getAll().map { entities -> entities.map { it.toMagnetData() } }

    suspend fun insertMagnet(data: MagnetData) {
        dao.insert(data.toEntity())
    }

    suspend fun updateMagnet(data: MagnetData) {
        dao.update(data.toEntity())
    }

    suspend fun updatePosition(id: String, x: Float, y: Float) {
        dao.updatePosition(id, x, y)
    }

    suspend fun deleteMagnet(id: String) {
        dao.delete(id)
    }

    suspend fun insertSampleIfEmpty(context: Context) {
        if (dao.getAll().firstOrNull().isNullOrEmpty()) {
            sampleMagnets.forEach { insertMagnet(it) }
        }
    }
}

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
    MagnetData("santorini", "Santorini","Summer '24",Color(0xFFFF8F00), Color(0xFFFFF8E1), MagnetShape.Postcard, 148f, 380f,  2.5f, elevation = 5.dp)
)