package com.example.tourtailor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.example.tourtailor.ui.auth.AuthViewModel

@Composable
fun SplashScreen(authViewModel: AuthViewModel, navController: NavHostController) {
    LaunchedEffect(Unit) {
        if (authViewModel.isUserLoggedIn()) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
    // Add UI for splash screen if needed
}