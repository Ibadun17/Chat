package com.example.tugas1.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val sender: String,    // "user" or "shop"
    val text: String? = null,
    val imageUrl: String? = null
)

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun sendTextMessage(text: String) {
        if (text.isBlank()) return

        // add user message
        val updated = _messages.value + ChatMessage(sender = "user", text = text)
        _messages.value = updated

        // simulate auto-reply from shop
        sendAutoReply()
    }

    fun sendImageMessage(uri: Uri) {
        // In a real app you'd upload the image and get URL back. For demo, use uri.toString()
        val imageUrl = uri.toString()
        val updated = _messages.value + ChatMessage(sender = "user", imageUrl = imageUrl)
        _messages.value = updated

        // simulate auto-reply from shop
        sendAutoReply()
    }

    private fun sendAutoReply() {
        viewModelScope.launch {
            delay(1200) // sim delay
            val reply = ChatMessage(sender = "shop", text = "Terima kasih telah menghubungi toko kami! Ada yang bisa kami bantu?")
            _messages.value = _messages.value + reply
        }
    }
}
