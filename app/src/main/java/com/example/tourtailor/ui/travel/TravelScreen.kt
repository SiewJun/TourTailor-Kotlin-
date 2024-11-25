package com.example.tourtailor.ui.travel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tourtailor.R
import com.example.tourtailor.data.entity.TravelPackageEntity
import com.example.tourtailor.ui.theme.AppTypography
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelScreen(viewModel: TravelPackageViewModel, navController: NavController) {
    val travelPackages by viewModel.travelPackages.collectAsState()
    val isRefreshing = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.travel_header),
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.W900)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) { innerPadding ->
        SwipeRefresh(
            state = SwipeRefreshState(isRefreshing.value),
            onRefresh = {
                isRefreshing.value = true
                viewModel.refreshPackages()
                isRefreshing.value = false
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 20.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                items(travelPackages) { travelPackage ->
                    TravelPackageCard(travelPackage, onPackageClick = {
                        navController.navigate("travel_package_details/${travelPackage.id}")
                    })
                }
            }
        }
    }
}

@Composable
fun TravelPackageCard(
    travelPackage: TravelPackageEntity,
    onPackageClick: () -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPackageClick)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = travelPackage.imageUrl),
                    contentDescription = travelPackage.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)),
                                startY = 100f
                            )
                        )
                )
                Text(
                    text = travelPackage.title,
                    style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = travelPackage.description, style = AppTypography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = travelPackage.pax.toString(),
                            style = AppTypography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Price: ${travelPackage.price}",
                        style = AppTypography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
