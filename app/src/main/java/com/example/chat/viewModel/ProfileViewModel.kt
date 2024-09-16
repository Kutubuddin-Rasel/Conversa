package com.example.chat.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) :ViewModel() {
    private val _selectedImageUri=MutableStateFlow<Uri?>(null)
    val selectedImageUri:StateFlow<Uri?> =_selectedImageUri
    private val _username=MutableStateFlow("")
    val username:StateFlow<String> =_username
    private val _phoneNumber=MutableStateFlow("")
    val phoneNumber:StateFlow<String> =_phoneNumber
init {
    getDetails()
   getImageuri()
}
    fun setUsername(name:String){
        _username.value=name
    }
    fun setPhoneNumber(phoneNumber:String){
        _phoneNumber.value=phoneNumber
    }
    fun setUri(uri:Uri){
        _selectedImageUri.value=uri
    }

    fun updateProfile(uri: Uri?,name:String,phonenumber:String){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("EVERY VALUES PASS ", listOf(name,phonenumber,uri).toString())
            profileRepository.updateProfile(uri,name,phonenumber)
        }
    }
    fun getDetails(){
        viewModelScope.launch(Dispatchers.IO) {
            val list=profileRepository.getDetails()
            _username.value=list[0]
            _phoneNumber.value=list[1]
        }
    }
    fun getImageuri(){
        viewModelScope.launch {
            val getUri=profileRepository.getImageUrl()
            if(getUri!=null){
                _selectedImageUri.value=getUri
            }
        }
    }
}