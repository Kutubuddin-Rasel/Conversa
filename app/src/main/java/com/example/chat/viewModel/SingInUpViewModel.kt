package com.example.chat.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SingInUpViewModel @Inject constructor() :ViewModel() {
    private val _email=MutableStateFlow("")
    val email:StateFlow<String> =_email

    private val _password=MutableStateFlow("")
    val password:StateFlow<String> =_password

    fun setEmail(email:String){
        _email.value=email
    }

    fun setPassword(password:String){
        _password.value=password
    }
}