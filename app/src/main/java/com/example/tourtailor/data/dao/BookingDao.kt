package com.example.tourtailor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tourtailor.data.entity.BookingEntity

@Dao
interface BookingDao {
    @Insert
    suspend fun insertBooking(booking: BookingEntity)

    @Query("SELECT * FROM bookings WHERE userId = :userId")
    suspend fun getUserBookings(userId: String): List<BookingEntity>
}
