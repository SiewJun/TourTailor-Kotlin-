package com.example.tourtailor.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val packageId: Int,
    val status: String
)
