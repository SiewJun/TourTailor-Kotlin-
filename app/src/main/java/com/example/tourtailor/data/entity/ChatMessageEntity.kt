package com.example.tourtailor.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageId: Int,
    val userId: String,
    val message: String,
    val timestamp: Long
)
