package com.example.tourtailor.ui.payment

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tourtailor.ui.booking.BookingViewModel
import com.example.tourtailor.ui.travel.TravelPackageViewModel
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    packageId: String,
    navController: NavController,
    bookingViewModel: BookingViewModel,
    travelPackageViewModel: TravelPackageViewModel
) {
    val travelPackage by travelPackageViewModel.getTravelPackageById(packageId).collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(packageId) {
        travelPackageViewModel.getTravelPackageById(packageId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        text = "Payment",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W900)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Secure Payment",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(50.dp))
            travelPackage?.let {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Confirm your booking for package: ${it.title}",
                            style = TextStyle(
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Pax: ${it.pax}",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Price: ${it.price}",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = {
                                val userId = FirebaseAuth.getInstance().currentUser?.uid
                                if (userId != null) {
                                    bookingViewModel.createBooking(userId, packageId.toInt())
                                }
                                navController.navigate("home")
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Pay Now",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            } ?: run {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Loading package details...",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
