package com.example.chat.data

import android.net.Uri

data class user(
    val userid:String,
    val username:String,
    val phoneNumber:String,
    val userImage: String,
    val searchUserName:String
){
    constructor():this("","","","","")
}
