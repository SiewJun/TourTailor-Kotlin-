package com.example.tourtailor.ui.profile

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tourtailor.R
import com.example.tourtailor.data.entity.UserProfileEntity
import com.example.tourtailor.ui.auth.AuthViewModel
import com.example.tourtailor.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewScreen(
    authViewModel: AuthViewModel,
    userProfileViewModel: UserProfileViewModel,
    navController: NavController,
    uid: String
) {
    var userProfile by remember { mutableStateOf<UserProfileEntity?>(null) }

    LaunchedEffect(uid) {
        userProfileViewModel.getUserProfile(uid) { profile ->
            userProfile = profile
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = stringResource(R.string.profile_header),
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.W900)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("profile/$uid") },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.onBackground)
                    }
                    IconButton(
                        onClick = {
                            authViewModel.logout {
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            ProfileImage(image = userProfile?.profileImage)
            Spacer(modifier = Modifier.height(24.dp))
            ProfileDetails(userProfile)
        }
    }
}

@Composable
fun ProfileImage(image: String?) {
    if (image != null) {
        val imageBitmap = remember(image) {
            val bytes = Base64.decode(image, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
        Image(
            bitmap = imageBitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
        )
    } else {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f), CircleShape)
        ) {
            Text(
                text = "No Image",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun ProfileDetails(userProfile: UserProfileEntity?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileDetailRow(label = "First Name", value = userProfile?.firstName ?: "N/A")
            ProfileDetailRow(label = "Last Name", value = userProfile?.lastName ?: "N/A")
            ProfileDetailRow(label = "NRIC", value = userProfile?.nric ?: "N/A")
            ProfileDetailRow(label = "Phone Number", value = userProfile?.phoneNumber ?: "N/A")
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
    }
}
