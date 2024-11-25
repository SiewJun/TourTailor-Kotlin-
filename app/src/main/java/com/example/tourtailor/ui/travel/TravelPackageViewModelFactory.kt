package com.example.tourtailor.ui.travel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tourtailor.data.dao.TravelPackageDao

class TravelPackageViewModelFactory(private val travelPackageDao: TravelPackageDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelPackageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TravelPackageViewModel(travelPackageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
