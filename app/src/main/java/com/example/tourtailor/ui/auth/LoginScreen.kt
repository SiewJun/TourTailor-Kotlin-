package com.example.tourtailor.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tourtailor.R
import com.example.tourtailor.ui.theme.AppTypography
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(authViewModel: AuthViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.login_header),
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
            Spacer(modifier = Modifier.height(16.dp)) // Add some space between TextFields
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.login_password_label)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )
            Button(
                onClick = {
                    authViewModel.login(email, password) { success, error ->
                        if (success) {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            currentUser?.let { user ->
                                navController.navigate("home")
                            }
                        } else {
                            message = error ?: "Login failed"
                        }
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
                Text(stringResource(R.string.login_login))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don't have an account?",
                    style = AppTypography.bodyMedium)
                TextButton(
                    onClick = { navController.navigate("register") }
                ) { Text(stringResource(R.string.login_register)) }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Forgot your password?",
                    style = AppTypography.bodyMedium)
                TextButton(
                    onClick = { navController.navigate("reset") },
                ) { Text(stringResource(R.string.login_reset_password)) }
            }
            Text(text = message, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
