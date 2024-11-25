package com.example.tourtailor.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tourtailor.data.dao.ReviewDao
import com.example.tourtailor.data.entity.ReviewEntity
import kotlinx.coroutines.launch

class ReviewViewModel(private val reviewDao: ReviewDao) : ViewModel() {
    fun leaveReview(review: ReviewEntity) {
        viewModelScope.launch {
            val existingReview = reviewDao.getUserReview(review.userId, review.bookingId)
            if (existingReview == null) {
                reviewDao.insertReview(review)
            } else {
                val updatedReview = existingReview.copy(
                    rating = review.rating,
                    reviewText = review.reviewText
                )
                reviewDao.updateReview(updatedReview)
            }
        }
    }

    fun getReviewsForPackage(packageId: Int, callback: (List<ReviewEntity>) -> Unit) {
        viewModelScope.launch {
            val reviews = reviewDao.getReviewsForPackage(packageId)
            callback(reviews)
        }
    }

    fun getUserReview(userId: String, bookingId: Int, callback: (ReviewEntity?) -> Unit) {
        viewModelScope.launch {
            val review = reviewDao.getUserReview(userId, bookingId)
            callback(review)
        }
    }
}

class ReviewViewModelFactory(private val reviewDao: ReviewDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            return ReviewViewModel(reviewDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
