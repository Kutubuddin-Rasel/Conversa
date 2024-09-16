package com.example.chat.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.data.user
import com.example.chat.repository.SearchUserRepository
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(private val searchUserRepository: SearchUserRepository) :
    ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    val userdetails = searchUserRepository.userdetails

    fun setUsername(name: String) {
        _username.value = name
    }

    fun searchUser(username: String) {
        viewModelScope.launch {
            searchUserRepository.SearchUser(username)
        }
    }
}
