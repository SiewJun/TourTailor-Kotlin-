package com.example.tourtailor.ui.travel

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.tourtailor.data.entity.ReviewEntity
import com.example.tourtailor.ui.review.ReviewViewModel
import com.example.tourtailor.ui.theme.AppTypography
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelPackageDetailsScreen(
    viewModel: TravelPackageViewModel,
    reviewViewModel: ReviewViewModel,
    packageId: String,
    navController: NavController
) {
    val travelPackage by viewModel.getTravelPackageById(packageId).collectAsState(initial = null)
    val reviews = remember { mutableStateListOf<ReviewEntity>() }
    val coroutineScope = rememberCoroutineScope()

    travelPackage?.let {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    title = {
                        Text(
                            text = it.title,
                            style = AppTypography.titleMedium.copy(fontWeight = FontWeight.W900)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        val painter = rememberAsyncImagePainter(
                            model = it.imageUrl,
                        )
                        Image(
                            painter = painter,
                            contentDescription = it.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (painter.state is AsyncImagePainter.State.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Text(
                            text = it.title,
                            style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pax: ${it.pax}",
                            style = AppTypography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "Price: ${it.price}",
                            style = AppTypography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it.description,
                        style = AppTypography.bodyLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        color = Color.Transparent
                    ) {
                        Button(
                            onClick = { navController.navigate("payment/$packageId") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(CircleShape)
                        ) {
                            Text(
                                text = "Book Now",
                                style = AppTypography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Reviews",
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    coroutineScope.launch {
                        reviewViewModel.getReviewsForPackage(packageId.toInt()) {
                            reviews.clear()
                            reviews.addAll(it)
                        }
                    }
                }
                items(reviews) { review ->
                    ReviewItem(review)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier)
        }
    }
}

@Composable
fun ReviewItem(review: ReviewEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = review.userId.first().uppercase(),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = review.userId,
                style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        imageVector = if (index < review.rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = null,
                        tint = if (index < review.rating) Color(0xFFFFD700) else Color(0xFFB0B0B0),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.reviewText,
                style = AppTypography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
