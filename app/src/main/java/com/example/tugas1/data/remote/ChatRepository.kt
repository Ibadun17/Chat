package com.example.tugas1.data.remote

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

class ChatRepository(private val context: Context) {

    fun saveImage(uri: Uri): String {
        val input = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "img_${System.currentTimeMillis()}.jpg")

        val output = FileOutputStream(file)
        input?.copyTo(output)

        output.close()
        input?.close()

        return file.absolutePath  // nanti digunakan sebagai imageUrl
    }
}
