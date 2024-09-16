package com.example.chat.repository

import android.util.Log
import com.example.chat.util.AuthState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class AuthenticationRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {
    private val _authState = MutableStateFlow<AuthState?>(null)
    val authState: StateFlow<AuthState?> =_authState

    init {
       checkAuthState()
    }
    private fun checkAuthState() {
        if (firebaseAuth.currentUser == null) {
            _authState.value = AuthState.UnAuthenticated
        } else {
            _authState.value = AuthState.Autheticated
        }
    }
    suspend fun logIn(email:String,password:String,onclick:(value:Boolean)->Unit){
        Log.d("TEST","function called")
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _authState.value = AuthState.Autheticated
                    onclick(true)
                }
                else{
                    onclick(false)
                }
            }
    }
   suspend fun signIn(email:String,password:String,onclick:(value:Boolean)->Unit){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    onclick(true)
                }
                else{
                    onclick(false)
                }
            }
    }
    suspend fun logOut(){
        firebaseAuth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }
}