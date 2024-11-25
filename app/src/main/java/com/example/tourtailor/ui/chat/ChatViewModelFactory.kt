package com.example.tourtailor.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tourtailor.data.dao.ChatMessageDao

class ChatViewModelFactory(
    private val chatMessageDao: ChatMessageDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(chatMessageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}