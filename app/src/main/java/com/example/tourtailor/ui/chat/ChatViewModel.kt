package com.example.tourtailor.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourtailor.data.dao.ChatMessageDao
import com.example.tourtailor.data.entity.ChatMessageEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val chatMessageDao: ChatMessageDao) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessageEntity>>(emptyList())
    val messages: StateFlow<List<ChatMessageEntity>> = _messages

    fun sendMessage(packageId: Int, userId: String, message: String) {
        viewModelScope.launch {
            val chatMessage = ChatMessageEntity(
                packageId = packageId,
                userId = userId,
                message = message,
                timestamp = System.currentTimeMillis()
            )
            chatMessageDao.insertMessage(chatMessage)
            loadMessages(packageId)
        }
    }

    fun loadMessages(packageId: Int) {
        viewModelScope.launch {
            val messages = chatMessageDao.getMessagesForPackage(packageId)
            _messages.value = messages
        }
    }
}
