package com.example.chat.repository

import android.util.Log
import com.example.chat.data.chatroom
import com.example.chat.data.recentChat
import com.example.chat.data.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatsRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth, private val firebaseFirestore: FirebaseFirestore
) {
    private val _reccentchat=MutableStateFlow<List<recentChat>>(emptyList())
    val recentchat:StateFlow<List<recentChat>> =_reccentchat

    suspend fun currentUserIdDocuments() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            val reference = firebaseFirestore.collection("ChatRoom")
            val query: Query = reference.whereArrayContains("usersIds", currentUser.uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
            try {
                val querySnapshot = query.get().await()
                if (querySnapshot.isEmpty) {
                    Log.d("CurrentUserDocuments", "Nothing found to that Id")
                } else {
                    val recentChats = mutableListOf<recentChat>()
                    val userIds = querySnapshot.flatMap { it.toObject(chatroom::class.java).usersIds }
                        .filter { it != currentUser.uid }
                        .distinct()

                    val userDetails = getUsersDetails(userIds)

                    for (document in querySnapshot) {
                        val chatroom = document.toObject(chatroom::class.java)
                        val userid = getAnotherUserID(userids = chatroom.usersIds)
                        val (name,image) = userDetails[userid] ?: Pair("Unknown","")
                        recentChats.add(recentChat(name, userid, chatroom.lastMessage, chatroom.lastMessageSenderId, chatroom.timestamp,image))
                    }
                    _reccentchat.value = recentChats
                }
            } catch (e: Exception) {
                Log.e("CurrentUserDocuments", "Error fetching documents", e)
            }
        }
    }


    fun getAnotherUserID(userids: List<String>): String {
        val currentUser =firebaseAuth.currentUser
        if (currentUser == null || userids.size < 2) {
            return ""
        }
        return if (userids[0] == currentUser.uid) {
            userids[1]
        } else {
            userids[0]
        }
    }
    suspend fun getUsersDetails(userIds: List<String>): Map<String, Pair<String,String>> {
        val userDetails = mutableMapOf<String, Pair<String,String>>()
        val reference = FirebaseFirestore.getInstance().collection("Users")
        val batch = reference.whereIn(FieldPath.documentId(), userIds).get().await()

        batch.forEach { document ->
            val user = document.toObject(user::class.java)
            user?.let {
                userDetails[document.id] = Pair(it.username,it.userImage)// Fetching User Name, and Uri
            }
        }
        return userDetails
    }
}