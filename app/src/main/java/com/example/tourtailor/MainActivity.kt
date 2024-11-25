package com.example.tourtailor

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.tourtailor.data.database.AppDatabase
import com.example.tourtailor.data.entity.TravelPackageEntity
import com.example.tourtailor.navigation.AppNavigation
import com.example.tourtailor.ui.auth.AuthViewModel
import com.example.tourtailor.ui.booking.BookingViewModel
import com.example.tourtailor.ui.booking.BookingViewModelFactory
import com.example.tourtailor.ui.chat.ChatViewModel
import com.example.tourtailor.ui.chat.ChatViewModelFactory
import com.example.tourtailor.ui.profile.UserProfileViewModel
import com.example.tourtailor.ui.profile.UserProfileViewModelFactory
import com.example.tourtailor.ui.review.ReviewViewModel
import com.example.tourtailor.ui.review.ReviewViewModelFactory
import com.example.tourtailor.ui.theme.TourtailorTheme
import com.example.tourtailor.ui.travel.TravelPackageViewModel
import com.example.tourtailor.ui.travel.TravelPackageViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var travelPackageViewModel: TravelPackageViewModel
    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var reviewViewModel: ReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "tourtailor-database"
        ).addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_4_5, AppDatabase.MIGRATION_5_6)
            .build()

        val travelPackages = listOf(
            TravelPackageEntity(
                imageUrl = "https://images.pexels.com/photos/2265876/pexels-photo-2265876.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                title = "Croatia",
                description = "Split-Dalmatia County is a central-southern Dalmatian county in Croatia. The administrative center is Split\n" + "7 May 2024",
                pax = 5,
                price = 13000.0
            ),
            TravelPackageEntity(
                imageUrl = "https://images.pexels.com/photos/2070485/pexels-photo-2070485.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                title = "Thailand",
                description = "It's known for tropical beaches, opulent royal palaces, ancient ruins and ornate temples displaying figures of Buddha\n" + "3 June 2024",
                pax = 7,
                price = 10000.0
            ),
            TravelPackageEntity(
                imageUrl = "https://images.pexels.com/photos/1010657/pexels-photo-1010657.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                title = "Santorini, Greece",
                description = "Santorini is one of the Cyclades islands in the Aegean Sea. It was devastated by a volcanic eruption in the 16th century BC, forever shaping its rugged landscape\n" + "21 June 2024",
                pax = 4,
                price = 15000.0,
            ),
            TravelPackageEntity(
                imageUrl = "https://images.pexels.com/photos/2253821/pexels-photo-2253821.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                title = "Bali, Indonesia",
                description = "Aerial View of Mountain\n" + "29 June 2024",
                pax = 4,
                price = 9000.0,
            ),
            TravelPackageEntity(
                imageUrl = "https://images.pexels.com/photos/38238/maldives-ile-beach-sun-38238.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                title = "Island Gazebo",
                description = "The Serene Retreat of Brown Hut Island Gazebo.\n" + "21 July 2024",
                pax = 4,
                price = 15000.0,
            ),
            TravelPackageEntity(
                imageUrl = "https://images.pexels.com/photos/2389473/pexels-photo-2389473.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                title = "Kuala Lumpur, Malaysia",
                description = "Kuala Lumpur is the capital of Malaysia. Its modern skyline is dominated by the 451m-tall Petronas Twin Towers, a pair of glass-and-steel-clad skyscrapers with Islamic motifs.\n" + "31 August 2024",
                pax = 6,
                price = 9500.0,
            )
        )

        lifecycleScope.launch {
            val existingPackages = db.travelPackageDao().getAllTravelPackages()
            if (existingPackages.isEmpty()) {
                db.travelPackageDao().insertTravelPackages(travelPackages)
            }
        }

        val travelPackageViewModelFactory = TravelPackageViewModelFactory(db.travelPackageDao())
        travelPackageViewModel = ViewModelProvider(this, travelPackageViewModelFactory)
            .get(TravelPackageViewModel::class.java)

        val bookingViewModelFactory = BookingViewModelFactory(db.bookingDao(), db.travelPackageDao())
        bookingViewModel = ViewModelProvider(this, bookingViewModelFactory)
            .get(BookingViewModel::class.java)

        val userProfileViewModelFactory = UserProfileViewModelFactory(db.userProfileDao())
        userProfileViewModel = ViewModelProvider(this, userProfileViewModelFactory)
            .get(UserProfileViewModel::class.java)

        val chatViewModelFactory = ChatViewModelFactory(db.chatMessageDao())
        chatViewModel = ViewModelProvider(this, chatViewModelFactory)
            .get(ChatViewModel::class.java)

        val reviewViewModelFactory = ReviewViewModelFactory(db.reviewDao())
        reviewViewModel = ViewModelProvider(this, reviewViewModelFactory)
            .get(ReviewViewModel::class.java)

        setContent {
            val authViewModel: AuthViewModel = viewModel()

            TourtailorTheme {
                AppNavigation(
                    authViewModel = authViewModel,
                    userProfileViewModel = userProfileViewModel,
                    travelPackageViewModel = travelPackageViewModel,
                    bookingViewModel = bookingViewModel,
                    chatViewModel = chatViewModel,
                    reviewViewModel = reviewViewModel,
                )
            }
        }
    }

    private fun enableEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            WindowCompat.setDecorFitsSystemWindows(window, false)
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }
}
