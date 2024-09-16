package com.example.chat.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.repository.ChatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(private val chatsRepository: ChatsRepository) :ViewModel() {
    private val _selectedImageUri=MutableStateFlow<Uri?>(null)
    val selectedImageUri:StateFlow<Uri?> =_selectedImageUri
    val recentchat = chatsRepository.recentchat
    init {
       recentchat()
    }
    fun recentchat() {
        viewModelScope.launch(Dispatchers.IO) {
            chatsRepository.currentUserIdDocuments()
        }
    }
}