package com.example.khetguru.domain.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri)
        return inputStream?.let {
            val file = File(context.cacheDir, "uploaded_image.jpg")
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            file
        }
    }
}
