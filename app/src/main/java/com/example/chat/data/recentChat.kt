package com.example.chat.data


import com.google.firebase.Timestamp

data class recentChat(
    val username:String,
    val userid:String,
    val lastMessage:String,
    val lastMessageSendId:String,
    val timestamp: Timestamp,
    val Image: String
) {
    constructor():this("","","","", Timestamp.now(),"")
}