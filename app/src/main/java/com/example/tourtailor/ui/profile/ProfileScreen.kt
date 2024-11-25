package com.example.tourtailor.ui.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tourtailor.R
import com.example.tourtailor.data.entity.UserProfileEntity
import com.example.tourtailor.ui.theme.AppTypography
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfileViewModel: UserProfileViewModel,
    navController: NavController,
    uid: String
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var nric by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var profileImage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            profileImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
    }

    LaunchedEffect(uid) {
        userProfileViewModel.getUserProfile(uid) { userProfile ->
            if (userProfile != null) {
                firstName = userProfile.firstName
                lastName = userProfile.lastName
                nric = userProfile.nric
                phoneNumber = userProfile.phoneNumber
                profileImage = userProfile.profileImage
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_header),
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.W900)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            profileImage?.let {
                val imageBitmap = remember(it) {
                    val bytes = Base64.decode(it, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { imagePickerLauncher.launch("image/*") }
                        .padding(bottom = 20.dp)
                        .clip(CircleShape)
                )
            } ?: Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f), CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") }
            ) {
                Text(
                    text = "Add Image",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            OutlinedTextField(
                value = nric,
                onValueChange = { nric = it },
                label = { Text("NRIC") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            Button(
                onClick = {
                    val userProfile = UserProfileEntity(
                        uid = uid,
                        firstName = firstName,
                        lastName = lastName,
                        nric = nric,
                        phoneNumber = phoneNumber,
                        profileImage = profileImage
                    )
                    userProfileViewModel.insertUserProfile(userProfile)
                    showSuccessDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text("Update Profile")
            }
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Success") },
                text = { Text("Profile updated successfully") },
                confirmButton = {
                    TextButton(
                        onClick = { showSuccessDialog = false }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
