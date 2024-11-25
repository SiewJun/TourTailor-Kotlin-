package com.example.tourtailor.ui.travel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourtailor.data.dao.TravelPackageDao
import com.example.tourtailor.data.entity.TravelPackageEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TravelPackageViewModel(private val travelPackageDao: TravelPackageDao) : ViewModel() {

    private val _travelPackages = MutableStateFlow<List<TravelPackageEntity>>(emptyList())
    val travelPackages: StateFlow<List<TravelPackageEntity>> = _travelPackages

    init {
        loadTravelPackages()
    }

    fun loadTravelPackages() {
        viewModelScope.launch {
            _travelPackages.value = travelPackageDao.getAllTravelPackages()
        }
    }

    fun refreshPackages() {
        viewModelScope.launch {
            // Simulate a network refresh or re-fetch the data from the database
            _travelPackages.value = travelPackageDao.getAllTravelPackages()
        }
    }

    fun getTravelPackageById(packageId: String): StateFlow<TravelPackageEntity?> {
        val travelPackageFlow = MutableStateFlow<TravelPackageEntity?>(null)
        viewModelScope.launch {
            travelPackageFlow.value = travelPackageDao.getTravelPackageById(packageId)
        }
        return travelPackageFlow
    }
}