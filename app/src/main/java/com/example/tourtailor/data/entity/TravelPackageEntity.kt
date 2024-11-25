package com.example.tourtailor.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travel_packages")
data class TravelPackageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUrl: String,
    val title: String,
    val description: String,
    val pax: Int,
    val price: Double
)
