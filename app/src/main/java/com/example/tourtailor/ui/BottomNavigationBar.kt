package com.example.tourtailor.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth

sealed class BottomNavItem(var route: String, var icon: ImageVector, var title: String) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Home")
    object Travel : BottomNavItem("travel", Icons.Filled.DateRange, "Travel")
    object Booking : BottomNavItem("booking", Icons.Filled.Favorite, "Booking")
    object Profile : BottomNavItem("profile_view", Icons.Filled.AccountCircle, "Profile")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Travel,
        BottomNavItem.Booking,
        BottomNavItem.Profile
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentDestination?.route?.startsWith(item.route) == true,
                onClick = {
                    navController.navigate(
                        when (item.route) {
                            "profile_view", "booking" -> "${item.route}/${FirebaseAuth.getInstance().currentUser?.uid}"
                            else -> item.route
                        }
                    ) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
