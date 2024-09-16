package com.example.chat.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chat.Routes
import com.example.chat.data.recentChat
import com.example.chat.util.formatTimestampTotime
import com.example.chat.viewModel.ChatsViewModel

@Composable
fun Chats(navController: NavController) {
    val chatsViewModel: ChatsViewModel = hiltViewModel()
    val recentchat = chatsViewModel.recentchat.collectAsState()
    Box {
        LazyColumn {
            Log.d("CALLINGFUNTICON", "RECENTCHAT CALLING- ${recentchat.value}")
            items(recentchat.value) {
                message(it, navController,chatsViewModel)
            }
        }
    }
}
@Composable
fun message(recentChat: recentChat, navController: NavController, chatsViewModel: ChatsViewModel) {
    Box(
        modifier = Modifier.padding(start = 8.dp, top = 5.dp)
            .clickable {
                navController.navigate("${Routes.chatscreen}/${recentChat.username}/${recentChat.userid}")
            }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = Uri.parse(recentChat.Image), // Use the URL here
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .width(60.dp) // Explicit width
                        .height(60.dp) // Explicit height
                        .clip(CircleShape), // Optional: Clip to a circle if needed
                    contentScale = ContentScale.Crop // Or ContentScale.Inside
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = recentChat.username,
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        ), modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = recentChat.lastMessage,
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Column {
                    Text(
                        formatTimestampTotime(recentChat.timestamp),
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp),
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.DarkGray
                    )
                }
            }
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(1.dp)
//                    .background(Color.Black)
//            )
        }
    }
}
