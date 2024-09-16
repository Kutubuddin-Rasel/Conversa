package com.example.chat.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.repository.AuthenticationRepository
import com.example.chat.util.AuthState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository):ViewModel() {
    val authstate=authenticationRepository.authState
    fun logIn(email:String,password:String,onclick:(value:Boolean)->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.logIn(email,password){
                if(it){
                    onclick(true)
                }
                else{
                    onclick(false)
                    //_authState.value=AuthState.error("LogIn Failed")
                }
            }
        }
    }

    fun signIn(email:String,password:String,onclick:(value:Boolean)->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.signIn(email,password){
                if(it){
                    onclick(true)
                }
                else{
                    onclick(false)
                   // _authState.value=AuthState.error("SignIn Failed")

                }
            }
        }
    }
    fun logOut(){
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.logOut()
        }
    }

}