package com.example.tourtailor.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tourtailor.ui.auth.AuthViewModel
import com.example.tourtailor.ui.auth.LoginScreen
import com.example.tourtailor.ui.auth.RegisterScreen
import com.example.tourtailor.ui.auth.ResetScreen
import com.example.tourtailor.ui.home.HomeScreen
import com.example.tourtailor.ui.profile.ProfileScreen
import com.example.tourtailor.ui.profile.UserProfileViewModel
import com.example.tourtailor.ui.BottomNavigationBar
import com.example.tourtailor.ui.SplashScreen
import com.example.tourtailor.ui.booking.BookingScreen
import com.example.tourtailor.ui.booking.BookingViewModel
import com.example.tourtailor.ui.chat.ChatScreen
import com.example.tourtailor.ui.chat.ChatViewModel
import com.example.tourtailor.ui.payment.PaymentScreen
import com.example.tourtailor.ui.travel.TravelPackageViewModel
import com.example.tourtailor.ui.travel.TravelScreen
import com.example.tourtailor.ui.profile.ProfileViewScreen
import com.example.tourtailor.ui.review.ReviewScreen
import com.example.tourtailor.ui.review.ReviewViewModel
import com.example.tourtailor.ui.travel.TravelPackageDetailsScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    userProfileViewModel: UserProfileViewModel,
    travelPackageViewModel: TravelPackageViewModel,
    bookingViewModel: BookingViewModel,
    chatViewModel: ChatViewModel,
    reviewViewModel: ReviewViewModel,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = when (currentRoute) {
        "login", "register", "reset" -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "splash", modifier = Modifier.padding(innerPadding)) {
            composable("splash") {
                SplashScreen(authViewModel, navController)
            }
            composable("login") {
                LoginScreen(authViewModel, navController)
            }
            composable("register") {
                RegisterScreen(authViewModel, navController)
            }
            composable("reset") {
                ResetScreen(authViewModel, navController)
            }
            composable("home") {
                HomeScreen(navController)
            }
            composable("travel") {
                TravelScreen(travelPackageViewModel, navController)
            }
            composable("booking/{uid}") { backStackEntry ->
                val uid = backStackEntry.arguments?.getString("uid") ?: ""
                BookingScreen(bookingViewModel, uid, navController)
            }
            composable("profile/{uid}") { backStackEntry ->
                val uid = backStackEntry.arguments?.getString("uid") ?: ""
                ProfileScreen(userProfileViewModel, navController, uid)
            }
            composable("profile_view/{uid}") { backStackEntry ->
                val uid = backStackEntry.arguments?.getString("uid") ?: ""
                ProfileViewScreen(authViewModel, userProfileViewModel, navController, uid)
            }
            composable("travel_package_details/{packageId}") { backStackEntry ->
                val packageId = backStackEntry.arguments?.getString("packageId") ?: ""
                TravelPackageDetailsScreen(travelPackageViewModel, reviewViewModel, packageId, navController)
            }
            composable("payment/{packageId}") { backStackEntry ->
                val packageId = backStackEntry.arguments?.getString("packageId") ?: ""
                PaymentScreen(packageId, navController, bookingViewModel, travelPackageViewModel)
            }
            composable("chat/{packageId}/{uid}") { backStackEntry ->
                val packageId = backStackEntry.arguments?.getString("packageId")?.toIntOrNull() ?: 0
                val uid = backStackEntry.arguments?.getString("uid") ?: ""
                ChatScreen(packageId, uid, chatViewModel, navController)
            }
            composable("leave_review/{packageId}/{bookingId}") { backStackEntry ->
                val packageId = backStackEntry.arguments?.getString("packageId")?.toIntOrNull() ?: 0
                val bookingId = backStackEntry.arguments?.getString("bookingId")?.toIntOrNull() ?: 0
                val userId = FirebaseAuth.getInstance().currentUser?.uid

                if (userId != null) {
                    ReviewScreen(
                        reviewViewModel = reviewViewModel,
                        packageId = packageId,
                        bookingId = bookingId,
                        userId = userId,
                        navController = navController
                    )
                }
            }
        }
    }
}
