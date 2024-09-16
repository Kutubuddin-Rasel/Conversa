package com.example.chat.util

import android.content.Context
import android.widget.Toast

fun toast(context: Context, message:String){
    Toast.makeText(context,"$message", Toast.LENGTH_SHORT).show()
}
fun Authentication(email:String,password:String,context: Context): Boolean {
    val value: Boolean
    if(email.isEmpty()){ toast(context,"Email can't be empty") }
    if(password.isEmpty()){ toast(context,"Password can't be empty") }
    if(email.isEmpty()||password.isEmpty()){
        toast(context,"Enter Email and Password")
        value=false
    }
    else{
        value=true
    }
    return value
}
sealed class AuthState{
    object Autheticated:AuthState()
    object UnAuthenticated:AuthState()
    object Loading:AuthState()
    data class error(val message:String):AuthState()
}