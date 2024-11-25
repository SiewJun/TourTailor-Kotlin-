package com.example.tourtailor.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tourtailor.data.dao.BookingDao
import com.example.tourtailor.data.dao.TravelPackageDao

class BookingViewModelFactory(
    private val bookingDao: BookingDao,
    private val travelPackageDao: TravelPackageDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(bookingDao, travelPackageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
