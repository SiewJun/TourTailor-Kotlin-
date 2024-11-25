package com.example.tourtailor.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tourtailor.R
import com.example.tourtailor.ui.theme.AppTypography
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.home_header),
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.W900)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        HomeScreenContent(navController, innerPadding)
    }
}

@Composable
fun HomeScreenContent(navController: NavController, innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        GreetingSection()
        Spacer(modifier = Modifier.height(80.dp))
        FeaturedContent()
        CallToActionSection(navController)
    }
}

@Composable
fun GreetingSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val userName = FirebaseAuth.getInstance().currentUser?.uid ?: "Traveler"
        Text(
            text = "Welcome back, $userName!",
            style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun FeaturedContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Discover amazing places to visit!",
            style = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(getImageUrls().size) { index ->
                FeaturedImageItem(url = getImageUrls()[index])
            }
        }
    }
}

@Composable
fun FeaturedImageItem(url: String) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = url)
                .apply {
                    crossfade(true)
                    size(600, 300)
                }.build()
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(200.dp)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
    )
}

fun getImageUrls(): List<String> {
    return listOf(
        "https://images.pexels.com/photos/5326965/pexels-photo-5326965.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
        "https://images.pexels.com/photos/356808/pexels-photo-356808.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
        "https://images.pexels.com/photos/2659475/pexels-photo-2659475.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
    )
}

@Composable
fun CallToActionSection(navController: NavController) {
    Button(
        onClick = { navController.navigate("travel") },
        shape = RoundedCornerShape(50),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Default.Info, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Get Started")
    }
}
