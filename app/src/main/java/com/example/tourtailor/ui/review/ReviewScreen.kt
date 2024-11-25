package com.example.tourtailor.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tourtailor.data.entity.ReviewEntity
import com.example.tourtailor.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    reviewViewModel: ReviewViewModel,
    packageId: Int,
    bookingId: Int,
    userId: String,
    navController: NavController
) {
    var rating by remember { mutableStateOf(0f) }
    var reviewText by remember { mutableStateOf("") }
    var existingReview by remember { mutableStateOf<ReviewEntity?>(null) }

    LaunchedEffect(userId, bookingId) {
        reviewViewModel.getUserReview(userId, bookingId) { review ->
            existingReview = review
            review?.let {
                rating = it.rating
                reviewText = it.reviewText
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Leave a Review",
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.W900)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Rate your experience", style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                RatingBar(rating) { newRating ->
                    rating = newRating
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Your Review",
                    style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text("Write your review here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        val review = existingReview?.copy(
                            rating = rating,
                            reviewText = reviewText
                        ) ?: ReviewEntity(
                            userId = userId,
                            packageId = packageId,
                            bookingId = bookingId,
                            rating = rating,
                            reviewText = reviewText
                        )
                        reviewViewModel.leaveReview(review)
                        navController.popBackStack()
                    },
                    enabled = rating > 0 && reviewText.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Submit Review", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Star $i",
                tint = if (i <= rating) Color(0xFFFFD700) else Color(0xFFB0B0B0),
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onRatingChanged(i.toFloat())
                    }
                    .padding(4.dp)
            )
        }
    }
}
