package com.example.chat.screens

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.chat.data.user
import com.example.chat.viewModel.ChatRoomViewModel
import com.example.chat.viewModel.SearchUserViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchUser(navController: NavController) {
    val searchUserViewModel:SearchUserViewModel= hiltViewModel()
    val chatRoomViewModel:ChatRoomViewModel= hiltViewModel()
    val username by searchUserViewModel.username.collectAsState()
    val userlist by searchUserViewModel.userdetails.collectAsState()
    var searchQuery by remember { mutableStateOf(username) }
    LaunchedEffect(searchQuery) {
        // Debounce the search query
        delay(300) // Adjust the delay as needed
        searchUserViewModel.searchUser(searchQuery)
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = {searchUserViewModel.setUsername(it)
                        searchQuery=it},
                    placeholder = { Text("Search User") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    searchUserViewModel.searchUser(username)
                }) {
                    Icon(imageVector = Icons.Default.Search,contentDescription = "",modifier = Modifier.size(35.dp))
                }
            }
            LazyColumn {
               items(userlist){
                   User(it,navController,chatRoomViewModel)
               }
            }
        }

    }
}
@Composable
fun User(
    user: user,
    navController: NavController,
    chatRoomViewModel: ChatRoomViewModel
){
    Column(
        modifier = Modifier.padding(0.dp).fillMaxWidth().clickable {
            chatRoomViewModel.chatRoom(user.userid){
                if(it){
                    chatRoomViewModel.chatRoom(user.userid){
                        navController.navigate("${Routes.chatscreen}/${user.username}/${user.userid}")
                    }
                }
            }
        }
    ){
        Row(modifier = Modifier.padding(10.dp)) {
            AsyncImage(
                model = Uri.parse(user.userImage), contentDescription = null, modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .clip(
                    CircleShape),
                contentScale = ContentScale.Crop
            )
           // Icon(imageVector = Icons.Default.Face, contentDescription = "", modifier = Modifier.size(40.dp))
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                text = user.username,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = user.phoneNumber,
                    style = TextStyle(
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}