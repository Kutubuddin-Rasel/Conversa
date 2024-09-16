package com.example.chat.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.repository.ChatScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatScreenViewModel @Inject constructor(private val chatScreenRepository: ChatScreenRepository) :ViewModel() {
    val currentUserChatList=chatScreenRepository.currentUserChatList
    val otherUserChatList=chatScreenRepository.otherUserChatList
    private val _chat=MutableStateFlow("")
    val chat:StateFlow<String> =_chat

    fun setChat(value:String){
        _chat.value=value
    }
    fun update(userid:String,message:String){
        viewModelScope.launch(Dispatchers.IO) {
            chatScreenRepository.update(userid,message)
        }
    }
    fun getchats(userid:String){
        viewModelScope.launch(Dispatchers.IO){
            chatScreenRepository.getChats(userid)
        }
    }
}