package com.example.tourtailor.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourtailor.data.dao.BookingDao
import com.example.tourtailor.data.dao.TravelPackageDao
import com.example.tourtailor.data.entity.BookingEntity
import com.example.tourtailor.data.entity.TravelPackageEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val bookingDao: BookingDao,
    private val travelPackageDao: TravelPackageDao
) : ViewModel() {

    private val _userBookings = MutableStateFlow<List<BookingWithPackage>>(emptyList())
    val userBookings: StateFlow<List<BookingWithPackage>> = _userBookings

    fun createBooking(userId: String, packageId: Int) {
        viewModelScope.launch {
            val booking = BookingEntity(
                userId = userId,
                packageId = packageId,
                status = "Confirmed"
            )
            bookingDao.insertBooking(booking)
        }
    }

    fun getUserBookings(userId: String) {
        viewModelScope.launch {
            val bookings = bookingDao.getUserBookings(userId)
            val bookingsWithPackages = bookings.map { booking ->
                val travelPackage = travelPackageDao.getTravelPackageById(booking.packageId.toString())
                travelPackage?.let {
                    BookingWithPackage(booking, it)
                }
            }.filterNotNull()
            _userBookings.value = bookingsWithPackages
        }
    }
}

data class BookingWithPackage(
    val booking: BookingEntity,
    val travelPackage: TravelPackageEntity
)
