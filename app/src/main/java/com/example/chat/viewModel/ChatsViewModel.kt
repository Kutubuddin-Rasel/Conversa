package com.example.chat.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.repository.ChatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(private val chatsRepository: ChatsRepository) :ViewModel() {
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