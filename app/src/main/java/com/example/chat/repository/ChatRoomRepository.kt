package com.example.chat.repository

import com.example.chat.data.chatroom
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ChatRoomRepository @Inject constructor(private val firebaseAuth: FirebaseAuth,private val firebaseFirestore: FirebaseFirestore) {

    private fun setChatid(userid:String):String{
        val currentUser=firebaseAuth.currentUser
        var chatId=""
        currentUser?.let {
            chatId=if(currentUser.uid.hashCode()<userid.hashCode()){
                return currentUser.uid+"_"+userid
            } else{
                userid+"_"+currentUser.uid
            }
        }
        return chatId
    }
    suspend fun chatRoom(userid: String,onclick:(value:Boolean)->Unit){
        val currentUser=firebaseAuth.currentUser
        currentUser?.let {
            val chatId=setChatid(userid)
            val reference=firebaseFirestore.collection("ChatRoom").document(chatId)
            reference.get()
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        onclick(true)
                       val chatroom=it.result.toObject(chatroom::class.java)
                        if(chatroom==null){
                            val newchatroom=chatroom(chatId,"","", listOf(currentUser.uid,userid),
                                Timestamp.now())
                            reference.set(newchatroom)
                        }
                    }
                    else{onclick(false)}
                }
        }
    }
}