package com.example.tourtailor.ui.booking

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tourtailor.R
import com.example.tourtailor.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookingScreen(
    bookingViewModel: BookingViewModel,
    userId: String,
    navController: NavController
) {
    val bookings by bookingViewModel.userBookings.collectAsState(initial = emptyList())

    LaunchedEffect(userId) {
        bookingViewModel.getUserBookings(userId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.booking_header),
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.W900)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            if (bookings.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_bookings_message),
                        style = AppTypography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(bookings) { bookingWithPackage ->
                        BookingItem(bookingWithPackage, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun BookingItem(bookingWithPackage: BookingWithPackage, navController: NavController) {
    val booking = bookingWithPackage.booking
    val travelPackage = bookingWithPackage.travelPackage
    var isFlipped by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { isFlipped = !isFlipped },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        AnimatedContent(
            targetState = isFlipped,
            transitionSpec = {
                ((slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut()))
                    .using(SizeTransform(clip = false))
            }, label = ""
        ) { targetIsFlipped ->
            if (targetIsFlipped) {
                Row(
                    modifier = Modifier.padding(32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate("chat/${travelPackage.id}/${booking.userId}")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Group Chat")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            navController.navigate("leave_review/${travelPackage.id}/${booking.id}")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Leave Review")
                    }
                }
            } else {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = travelPackage.title,
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Pax: ${travelPackage.pax}",
                                style = AppTypography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Price: ${travelPackage.price}",
                                style = AppTypography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Status: ${booking.status}",
                                style = AppTypography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}
