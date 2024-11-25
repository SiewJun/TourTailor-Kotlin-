package com.example.tourtailor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tourtailor.data.entity.UserProfileEntity

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfileEntity)

    @Update
    suspend fun updateUserProfile(userProfile: UserProfileEntity)

    @Query("SELECT * FROM user_profile WHERE uid = :uid LIMIT 1")
    suspend fun getUserProfile(uid: String): UserProfileEntity?
}
