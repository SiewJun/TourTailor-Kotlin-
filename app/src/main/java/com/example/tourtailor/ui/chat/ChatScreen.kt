package com.example.tourtailor.ui.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tourtailor.R
import com.example.tourtailor.data.entity.ChatMessageEntity
import com.example.tourtailor.ui.booking.BookingViewModel
import com.example.tourtailor.ui.theme.AppTypography
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    packageId: Int,
    userId: String,
    chatViewModel: ChatViewModel,
    navController: NavController
) {
    val messages by chatViewModel.messages.collectAsState()

    LaunchedEffect(packageId) {
        chatViewModel.loadMessages(packageId)
    }

    val sortedMessages = messages.sortedBy { it.timestamp }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.chat_header),
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.W900)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(sortedMessages) { message ->
                        ChatMessageItem(message, isCurrentUser = message.userId == userId)
                    }
                }
                SendMessageBar(packageId, userId, chatViewModel)
            }
        }
    )
}

@Composable
fun ChatMessageItem(message: ChatMessageEntity, isCurrentUser: Boolean) {
    val backgroundColor = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    val formattedTimestamp = remember(message.timestamp) {
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(message.timestamp))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isCurrentUser) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.userId.first().uppercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(12.dp))
                .padding(12.dp)
                .widthIn(max = 250.dp)
        ) {
            Text(
                text = message.message,
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                textAlign = TextAlign.Start
            )
            Text(
                text = formattedTimestamp,
                style = MaterialTheme.typography.bodySmall.copy(color = textColor, fontSize = 10.sp),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessageBar(packageId: Int, userId: String, chatViewModel: ChatViewModel) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier.weight(1f).padding(8.dp),
            placeholder = { Text("Type a message") },
            maxLines = 3,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        IconButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    chatViewModel.sendMessage(packageId, userId, messageText)
                    messageText = ""
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}
