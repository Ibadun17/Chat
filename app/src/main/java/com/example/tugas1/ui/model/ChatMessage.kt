package com.example.tugas1.ui.model

data class ChatMessage(
    val sender: String,      // "user" atau "shop"
    val text: String,        // isi pesan
    val imageUrl: String? = null   // untuk pesan gambar
)
