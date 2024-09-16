package com.example.chat.repository

import android.util.Log
import com.example.chat.data.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SearchUserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
)
{
    private val _userdetails= MutableStateFlow<List<user>>(emptyList())
    val userdetails:StateFlow<List<user>> =_userdetails
    suspend fun SearchUser(username: String){
        if(username.isEmpty()){
            _userdetails.value= emptyList()
            return
        }
        val currentUser=firebaseAuth.currentUser
        currentUser?.let {
            val reference=FirebaseFirestore.getInstance().collection("Users")
            val query: Query =reference.whereGreaterThanOrEqualTo("searchUserName",username.lowercase())
                .whereLessThanOrEqualTo("searchUserName",username.lowercase()+"\uf8ff")
            query.get().addOnSuccessListener {documents->
                    if(documents.isEmpty){
                        Log.d("SearchUser","No user with the $username")
                    } else{
                        val userdetails= mutableListOf<user>()
                        for(document in documents){
                            val details=document.toObject(user::class.java)
                            userdetails.add(details)
                        }
                        _userdetails.value=userdetails
                    }
                }
        }
    }
}
