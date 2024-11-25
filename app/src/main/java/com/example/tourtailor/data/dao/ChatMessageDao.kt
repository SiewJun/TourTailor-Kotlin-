package com.example.tourtailor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tourtailor.data.entity.ChatMessageEntity

@Dao
interface ChatMessageDao {
    @Insert
    suspend fun insertMessage(chatMessage: ChatMessageEntity)

    @Query("SELECT * FROM chat_messages WHERE packageId = :packageId ORDER BY timestamp ASC")
    suspend fun getMessagesForPackage(packageId: Int): List<ChatMessageEntity>
}
