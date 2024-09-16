package com.example.chat.repository

import android.util.Log
import com.example.chat.data.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class UserDetailsRepository @Inject constructor(private val firebaseAuth: FirebaseAuth,private val firebaseFirestore: FirebaseFirestore) {
    suspend fun SaveUser(username:String,phoneNumber:String,onClick:(value:Boolean)->Unit){
        val currentuser=firebaseAuth.currentUser
        currentuser?.let {
            val reference=firebaseFirestore.collection("Users").document(currentuser.uid)
            val user=user(currentuser.uid,username,phoneNumber,"",username.lowercase())
            reference.set(user)
                .addOnSuccessListener {
                    onClick(true)
                    Log.d("UserSave","User saved")
                }
                .addOnFailureListener { onClick(false) }
            reference.addSnapshotListener { value, error ->
                if(value==null || error!=null){
                    Log.d("UserSave","Failed")
                    if (error != null) {
                        Log.d("UserSave",error.message.toString())
                    }
                }
            }
        }
    }

}