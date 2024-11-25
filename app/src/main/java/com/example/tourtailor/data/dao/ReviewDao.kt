package com.example.tourtailor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tourtailor.data.entity.ReviewEntity

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Update
    suspend fun updateReview(review: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE packageId = :packageId")
    suspend fun getReviewsForPackage(packageId: Int): List<ReviewEntity>

    @Query("SELECT * FROM reviews WHERE userId = :userId AND bookingId = :bookingId")
    suspend fun getUserReview(userId: String, bookingId: Int): ReviewEntity?
}
