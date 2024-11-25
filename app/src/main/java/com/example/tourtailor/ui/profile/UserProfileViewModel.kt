package com.example.tourtailor.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tourtailor.data.dao.UserProfileDao
import com.example.tourtailor.data.entity.UserProfileEntity
import kotlinx.coroutines.launch

class UserProfileViewModel(private val userProfileDao: UserProfileDao) : ViewModel() {

    fun insertUserProfile(userProfile: UserProfileEntity) {
        viewModelScope.launch {
            userProfileDao.insertUserProfile(userProfile)
        }
    }

    fun getUserProfile(uid: String, onResult: (UserProfileEntity?) -> Unit) {
        viewModelScope.launch {
            val userProfile = userProfileDao.getUserProfile(uid)
            onResult(userProfile)
        }
    }
}

class UserProfileViewModelFactory(
    private val userProfileDao: UserProfileDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserProfileViewModel(userProfileDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
