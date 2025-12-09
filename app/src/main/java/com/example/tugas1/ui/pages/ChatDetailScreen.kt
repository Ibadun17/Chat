package com.example.tugas1.ui.pages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tugas1.ui.viewmodel.ChatViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    shopName: String,
    imageUrl: String, // ini kemungkinan sudah di-encode saat navigate; decode di sini
    navController: NavController,
    viewModel: ChatViewModel
) {
    var text by remember { mutableStateOf("") }
    val messages by viewModel.messages.collectAsState(initial = emptyList())

    // decode imageUrl if encoded
    val decodedShopImage = remember(imageUrl) { Uri.decode(imageUrl) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.sendImageMessage(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(shopName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { pickImageLauncher.launch("image/*") }) {
                    Icon(Icons.Filled.Image, contentDescription = "Upload Gambar")
                }

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Tulis pesan...") }
                )

                IconButton(onClick = {
                    if (text.isNotEmpty()) {
                        viewModel.sendTextMessage(text)
                        text = ""
                    }
                }) {
                    Icon(Icons.Filled.Send, contentDescription = "Send")
                }
            }
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // HEADER TOKO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F8F8))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = decodedShopImage,
                    contentDescription = "Shop Image",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(12.dp))

                Text(shopName, style = MaterialTheme.typography.titleMedium)
            }

            // LIST PESAN
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(messages) { msg ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (!msg.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = msg.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                contentScale = ContentScale.FillWidth
                            )
                        }

                        if (!msg.text.isNullOrBlank()) {
                            Text(
                                text = msg.text ?: "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                textAlign = if (msg.sender == "user") TextAlign.End else TextAlign.Start,
                                color = if (msg.sender == "user") Color.Black else Color.DarkGray
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
