package com.example.chat.repository

import android.util.Log
import com.example.chat.data.chat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatScreenRepository @Inject constructor(private val firebaseAuth: FirebaseAuth, private val firebaseFirestore: FirebaseFirestore) {
    private val _currentUserChatList=MutableStateFlow<List<chat>>(emptyList())
    val currentUserChatList:StateFlow<List<chat>> = _currentUserChatList
    private val _otherUserChatList=MutableStateFlow<List<chat>>(emptyList())
    val otherUserChatList:StateFlow<List<chat>> =_otherUserChatList

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
    suspend fun update(userid: String, message: String) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            val chatId = setChatid(userid)
            val reference = firebaseFirestore.collection("ChatRoom").document(chatId)
            try {
                reference.update(
                    "lastMessage", message,
                    "lastMessageSenderId",currentUser.uid,
                    "timestamp", Timestamp.now()
                ).await()  // Ensure you are using the appropriate coroutine extension function
                val reference2 = firebaseFirestore.collection("ChatRoom").document(chatId).collection("Chats")
                reference2.add(chat(currentUser.uid, message, Timestamp.now())).await()  // Ensure you are using the appropriate coroutine extension function

            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
        }
    }

    suspend fun getChats(userid: String){
        val currentUser=firebaseAuth.currentUser
        currentUser?.let {
            val chatId=setChatid(userid)
            val reference= firebaseFirestore.collection("ChatRoom").document(chatId).collection("Chats")
            val query:Query=reference.orderBy("timestamp",Query.Direction.ASCENDING)
            query.addSnapshotListener { value, error ->
                if(error!=null){
                    Log.e("GETSCHATDATA","Error fetching chats",error)
                    return@addSnapshotListener
                }
                if(value==null||value.isEmpty){
                    Log.d("GETSCHATDATA","No chats")
                    _currentUserChatList.value= emptyList()
                    _otherUserChatList.value= emptyList()
                    return@addSnapshotListener
                }
                val currentUserChatList= mutableListOf<chat>()
                val otherUsereChatList=mutableListOf<chat>()
                for(document in value.documents){
                    val chatdata=document.toObject(chat::class.java)
                    chatdata?.let {
                        if(chatdata.senderId==currentUser.uid){
                            currentUserChatList.add(chatdata)
                        } else{
                            otherUsereChatList.add(chatdata)
                        }
                    }
                }
                _currentUserChatList.value=currentUserChatList
                _otherUserChatList.value=otherUsereChatList
            }
        }
    }
}