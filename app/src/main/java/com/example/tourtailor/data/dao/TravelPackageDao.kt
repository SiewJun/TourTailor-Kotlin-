package com.example.tourtailor.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tourtailor.data.entity.TravelPackageEntity

@Dao
interface TravelPackageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTravelPackages(travelPackages: List<TravelPackageEntity>)

    @Query("SELECT * FROM travel_packages")
    suspend fun getAllTravelPackages(): List<TravelPackageEntity>

    @Query("SELECT * FROM travel_packages WHERE id = :packageId")
    suspend fun getTravelPackageById(packageId: String): TravelPackageEntity?
}
