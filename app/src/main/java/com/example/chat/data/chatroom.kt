package com.example.chat.data

import com.google.firebase.Timestamp

data class chatroom(
    val chatId:String,
    val lastMessageSenderId:String,
    val lastMessage:String,
    val usersIds:List<String>,
    val timestamp: Timestamp
) {
    constructor():this("","","", emptyList(),Timestamp.now())
}