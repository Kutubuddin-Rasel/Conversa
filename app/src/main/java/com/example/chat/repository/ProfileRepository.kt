package com.example.chat.repository

import android.net.Uri
import android.util.Log
import com.example.chat.data.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ProfileRepository @Inject constructor(private val firebaseAuth: FirebaseAuth
,private val firebaseFirestore: FirebaseFirestore
) {
    suspend fun updateProfile(uri: Uri?,username:String,phonenumber:String){
        val currentUser=firebaseAuth.currentUser
        currentUser?.let {
            try {
                if (uri != null && uri.scheme!="https") {
                    setImage(uri)
                    val image=getImageUrl().toString()
                    image?.let {
                        Log.d("EVERY VALUES PASS ", listOf(username,phonenumber,image).toString())
                        firebaseFirestore.collection("Users").document(currentUser.uid)
                            .update(
                                "username",
                                username,
                                "phoneNumber",
                                phonenumber,
                                "userImage",
                                image
                            )
                            .await()
                    }
                } else {
                    firebaseFirestore.collection("Users").document(currentUser.uid)
                        .update("username",username, "phoneNumber", phonenumber)
                        .await()
                }
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Error updating profile", e)
            }
        }
    }
suspend fun setImage(uri: Uri) = suspendCancellableCoroutine<Unit> { continuation ->
    val currentUser = firebaseAuth.currentUser
    if (currentUser == null) {
        continuation.resumeWithException(IllegalStateException("User not logged in"))
        return@suspendCancellableCoroutine
    }
    val storageRef = FirebaseStorage.getInstance().reference.child("Profile_pic/${currentUser.uid}")
    val uploadTask = storageRef.putFile(uri)
    uploadTask.addOnSuccessListener {
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            continuation.resume(Unit)
        }.addOnFailureListener {
            continuation.resumeWithException(it)
        }
    }.addOnFailureListener {
        continuation.resumeWithException(it)
    }.addOnCanceledListener {
        continuation.cancel()
    }
}

    suspend fun getImageUrl(): Uri? = suspendCancellableCoroutine { continuation ->
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            val storageReference = FirebaseStorage.getInstance().getReference("Profile_pic/${currentUser.uid}")

            storageReference.downloadUrl.addOnSuccessListener { uri ->
                continuation.resume(uri)
            }.addOnFailureListener { exception ->
                if (exception is StorageException && exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    // Image does not exist, return null
                    continuation.resume(null)
                } else {
                    continuation.resumeWithException(exception)
                }
            }.addOnCanceledListener {
                continuation.cancel()
            }
        } ?: continuation.resumeWithException(IllegalStateException("User not logged in"))
    }
    suspend fun getDetails():List<String> =  suspendCancellableCoroutine{continuation->
        val currentuser=firebaseAuth.currentUser
        currentuser?.let {
            val refernce=firebaseFirestore.collection("Users").document(currentuser.uid)
            refernce.get()
                .addOnSuccessListener {document->
                    val value=document.toObject(user::class.java)
                    if(value!=null){
                        continuation.resume(listOf(value.username,value.phoneNumber))
                    }
                    else{
                        continuation.resume(emptyList())
                    }
                }.addOnFailureListener{
                    continuation.resumeWithException(it)
                }
        }
    }
}