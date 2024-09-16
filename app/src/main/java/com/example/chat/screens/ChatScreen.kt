package com.example.chat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chat.R
import com.example.chat.Routes
import com.example.chat.util.formatTimestampTotime
import com.example.chat.viewModel.ChatScreenViewModel
import com.example.chat.viewModel.ChatsViewModel
import com.google.firebase.Timestamp


@Composable
fun ChatScreen(username: String, userid: String, navController: NavController) {
    val chatScreenViewModel:ChatScreenViewModel= hiltViewModel()
    val chat=chatScreenViewModel.chat.collectAsState()
    val currentUserChatList=chatScreenViewModel.currentUserChatList.collectAsState()
    val otherUserChatList=chatScreenViewModel.otherUserChatList.collectAsState()
    LaunchedEffect(userid) {
        chatScreenViewModel.getchats(userid)
    }
    val combinedChat=(currentUserChatList.value+otherUserChatList.value)
        .sortedByDescending { it.timestamp }
    var hasStartedTyping by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(16, 29, 77))
            ) {
                IconButton(
                    onClick = {navController.navigate(Routes.chats) }
                ){
                    Icon(painter = painterResource(R.drawable.back),
                        contentDescription = "back",
                        modifier = Modifier.size(25.dp),
                        tint = Color.White)
                }
                Text(
                    text = username,
                    modifier = Modifier.weight(1f),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(16, 29, 77))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value =chat.value,
                    onValueChange = {
                        if(it.isNotEmpty()){hasStartedTyping=true }
                        chatScreenViewModel.setChat(it) },
                    placeholder = { Text(text = if (hasStartedTyping) "Type a message..." else "Message") },
                    modifier = Modifier.weight(1f).background(Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            chatScreenViewModel.update(userid,chat.value)
                            chatScreenViewModel.setChat("")
                            chatScreenViewModel.getchats(userid)
                            //chatsViewModel.recentchat()
                        }
                    )
                )
                IconButton(onClick = {
                    chatScreenViewModel.update(userid,chat.value)
                    chatScreenViewModel.setChat("")
                    chatScreenViewModel.getchats(userid)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.send),
                        tint = Color.White,
                        contentDescription = "",
                        modifier = Modifier.size(25.dp),
                    )

                }
            }
        }
    )
    {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it),
            reverseLayout = true, // Scrolls from bottom to top
            contentPadding = PaddingValues(8.dp)
        ) {
            items(combinedChat){message->
                ChatMessageItem(message.message,message.senderId==userid,message.timestamp)
            }
        }
    }
}
@Composable
fun ChatMessageItem(message: String, isCurrentUser: Boolean, timestamp: Timestamp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.Start else Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isCurrentUser) Color(0xFFF2F7FB) else Color(0xFF3D4A7A),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = "$message\n${formatTimestampTotime(timestamp)}",
                color = if (isCurrentUser) Color(0xFF000E08) else Color.White
            )
        }
    }
}

