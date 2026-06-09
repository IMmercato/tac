package com.imesh.tac.magnet

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "magnets")
data class MagnetEntity(
    @PrimaryKey val id: String,
    val label: String,
    val subLabel: String,
    val colorArgb: Int,
    val accentArgb: Int,
    val shapeName: String,
    val posX: Float,
    val posY: Float,
    val rotation: Float,
    val elevationDp: Float,
    val imagePath: String?
) {
    fun toMagnetData(): MagnetData {
        val shape = when (shapeName) {
            "Arch" -> MagnetShape.Arch
            "Disc" -> MagnetShape.Disc
            "Shield" -> MagnetShape.Shield
            "Postcard" -> MagnetShape.Postcard
            "Heart" -> MagnetShape.Heart
            "FlagStrip" -> MagnetShape.FlagStrip
            else -> MagnetShape.Arch
        }
        return MagnetData(
            id = id,
            label = label,
            subLabel = subLabel,
            color = Color(colorArgb.toLong()),
            accentColor = Color(accentArgb),
            shape = shape,
            initialX = posX,
            initialY = posY,
            initialRotation = rotation,
            elevation = elevationDp.dp,
            imagePath = imagePath
        )
    }
}

fun MagnetData.toEntity(): MagnetEntity {
    val shapeName = when (shape) {
        MagnetShape.Arch -> "Arch"
        MagnetShape.Disc -> "Disc"
        MagnetShape.Shield -> "Shield"
        MagnetShape.Postcard -> "Postcard"
        MagnetShape.Heart -> "Heart"
        MagnetShape.FlagStrip -> "FlagStrip"
    }
    return MagnetEntity(
        id = id,
        label = label,
        subLabel = subLabel,
        colorArgb = color.value.toInt(),
        accentArgb = accentColor.value.toInt(),
        shapeName = shapeName,
        posX = initialX,
        posY = initialY,
        rotation = initialRotation,
        elevationDp = elevation.value,
        imagePath = imagePath
    )
}