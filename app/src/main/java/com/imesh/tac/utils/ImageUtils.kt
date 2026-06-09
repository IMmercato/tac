package com.imesh.tac.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    fun saveImageToInternalStorage(context: Context, uri: Uri, id: String): String? {
        val resolver: ContentResolver = context.contentResolver
        val inputStream = resolver.openInputStream(uri) ?: return null
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        val dir = File(context.filesDir, "magnet_images")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "$id.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        }
        return file.absolutePath
    }
}