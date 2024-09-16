package com.example.chat.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.repository.UserDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(private val userDetailsRepository: UserDetailsRepository): ViewModel() {
    private val _username=MutableStateFlow("")
    val username:StateFlow<String> =_username
    private val _phoneNumber=MutableStateFlow("")
    val phoneNumber:StateFlow<String> =_phoneNumber
    fun setPhoneNumber(phonenumber:String){
        _phoneNumber.value=phonenumber
    }
    fun setUsername(name:String){
        _username.value=name
    }
    fun SaveUser(username:String,phonenumber: String,onclick:(value:Boolean)->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            userDetailsRepository.SaveUser(username,phonenumber){
                onclick(it)
            }
        }
    }
}