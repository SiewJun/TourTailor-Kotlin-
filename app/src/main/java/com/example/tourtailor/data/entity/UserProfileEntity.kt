package com.example.tourtailor.data.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val uid: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImage: String?,  // Ensure this is nullable if it's optional
    val nric: String
)
