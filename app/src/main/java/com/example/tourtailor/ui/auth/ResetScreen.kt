package com.example.tourtailor.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tourtailor.R
import com.example.tourtailor.ui.theme.AppTypography

@Composable
fun ResetScreen(authViewModel: AuthViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column (
            modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        ){
            IconButton(
                onClick = { navController.popBackStack() }, // Add this line
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.reset_header),
                style = AppTypography.displaySmall.copy(fontWeight = FontWeight.W900),
                modifier = Modifier.padding(bottom = 70.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.login_email_label)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),// Add rounded corners
            )
            Button(
                onClick = {
                    authViewModel.resetPassword(email) { success, error ->
                        message = if (success) "Password reset email sent" else error ?: "Reset failed"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(R.string.login_reset_password))
            }
            Text(text = message, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
