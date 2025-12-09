package com.example.tugas1.ui.pages

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugas1.viewmodel.ProductViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

// DATA MODEL
data class ChatItem(
    val shopName: String,
    val lastMessage: String,
    val date: String,
    val unreadCount: Int,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController, productViewModel: ProductViewModel)
 {

    val chatList = listOf(
        ChatItem(
            "Nafasyh Boutique", "Halo kakak, terima kasih...", "15/11", 1,
            "https://i.pinimg.com/736x/d3/b2/81/d3b2818487c9cbe45333b966127c0c44.jpg"
        ),
        ChatItem(
            "H&M", "Terimakasih telah menghubungi kami...", "04/11", 1,
            "https://www.centralparkjakarta.com/upload/tenant/0h&m.jpg"
        ),
        ChatItem(
            "D Fashion", "Halo! Terima kasih...", "25/06", 1,
            "https://i.pinimg.com/736x/83/8a/3d/838a3dd58725af4953e5963db2ddb0db.jpg"
        )
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Chat") }) }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            items(chatList) { chat ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // FIX — Encode URL agar tidak crash
                            val encodedUrl = Uri.encode(chat.imageUrl)
                            navController.navigate("chat_detail/${chat.shopName}/$encodedUrl")
                        }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(chat.imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(chat.shopName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(
                            chat.lastMessage,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            maxLines = 1
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            chat.date,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        if (chat.unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = chat.unreadCount.toString(),
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Divider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}
