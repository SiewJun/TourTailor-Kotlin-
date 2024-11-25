package com.example.tourtailor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tourtailor.data.dao.BookingDao
import com.example.tourtailor.data.dao.ChatMessageDao
import com.example.tourtailor.data.dao.ReviewDao
import com.example.tourtailor.data.dao.TravelPackageDao
import com.example.tourtailor.data.dao.UserProfileDao
import com.example.tourtailor.data.entity.BookingEntity
import com.example.tourtailor.data.entity.ChatMessageEntity
import com.example.tourtailor.data.entity.TravelPackageEntity
import com.example.tourtailor.data.entity.UserProfileEntity
import com.example.tourtailor.data.entity.ReviewEntity

@Database(
    entities = [
        UserProfileEntity::class,
        TravelPackageEntity::class,
        BookingEntity::class,
        ChatMessageEntity::class,
        ReviewEntity::class,
    ],
    version = 5,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun travelPackageDao(): TravelPackageDao
    abstract fun bookingDao(): BookingDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user_profile ADD COLUMN profileImage TEXT")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE travel_packages (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, imageUrl TEXT NOT NULL, title TEXT NOT NULL, description TEXT NOT NULL, pax INTEGER NOT NULL, price REAL NOT NULL)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE bookings (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, userId TEXT NOT NULL, packageId INTEGER NOT NULL, status TEXT NOT NULL)")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE chat_messages (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, bookingId INTEGER NOT NULL, firstName TEXT NOT NULL, lastName TEXT NOT NULL, message TEXT NOT NULL, timestamp INTEGER NOT NULL)")
            }
        }

        val MIGRATION_5_6 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE reviews (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, userId TEXT NOT NULL, packageId INTEGER NOT NULL, bookingId INTEGER NOT NULL, rating REAL NOT NULL, reviewText TEXT NOT NULL)")
            }
        }
    }
}
