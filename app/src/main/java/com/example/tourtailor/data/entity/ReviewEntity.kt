package com.example.tourtailor.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val packageId: Int,
    val bookingId: Int,
    val rating: Float,
    val reviewText: String
)
