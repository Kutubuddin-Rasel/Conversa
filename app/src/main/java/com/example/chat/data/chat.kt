package com.example.chat.data

import com.google.firebase.Timestamp

data class chat(
    val senderId:String,
    val message:String,
    val timestamp: Timestamp
){
    constructor():this("","", Timestamp.now())
}
