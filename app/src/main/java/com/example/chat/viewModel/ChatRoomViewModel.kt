package com.example.chat.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.repository.ChatRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatRoomViewModel @Inject constructor(private val chatRoomRepository: ChatRoomRepository):ViewModel(){
    fun chatRoom(userid:String,onclick:(value:Boolean)->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            chatRoomRepository.chatRoom(userid){
                onclick(it)
            }
        }
    }
}